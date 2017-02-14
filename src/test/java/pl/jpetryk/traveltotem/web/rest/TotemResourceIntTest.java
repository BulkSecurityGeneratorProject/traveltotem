package pl.jpetryk.traveltotem.web.rest;

import pl.jpetryk.traveltotem.TraveltotemApp;

import pl.jpetryk.traveltotem.domain.Totem;
import pl.jpetryk.traveltotem.domain.User;
import pl.jpetryk.traveltotem.repository.TotemRepository;
import pl.jpetryk.traveltotem.service.TotemService;
import pl.jpetryk.traveltotem.service.dto.TotemDTO;
import pl.jpetryk.traveltotem.service.mapper.TotemMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TotemResource REST controller.
 *
 * @see TotemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TraveltotemApp.class)
public class TotemResourceIntTest {

    private static final Double DEFAULT_CREATION_LATITUDE = 1D;
    private static final Double UPDATED_CREATION_LATITUDE = 2D;

    private static final Double DEFAULT_CREATION_LONGITUDE = 1D;
    private static final Double UPDATED_CREATION_LONGITUDE = 2D;

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_DISTANCE_TRAVELLED = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISTANCE_TRAVELLED = new BigDecimal(1);

    @Inject
    private TotemRepository totemRepository;

    @Inject
    private TotemMapper totemMapper;

    @Inject
    private TotemService totemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTotemMockMvc;

    private Totem totem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TotemResource totemResource = new TotemResource();
        ReflectionTestUtils.setField(totemResource, "totemService", totemService);
        this.restTotemMockMvc = MockMvcBuilders.standaloneSetup(totemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Totem createEntity(EntityManager em) {
        Totem totem = new Totem()
                .creationLatitude(DEFAULT_CREATION_LATITUDE)
                .creationLongitude(DEFAULT_CREATION_LONGITUDE)
                .creationDate(DEFAULT_CREATION_DATE)
                .distanceTravelled(DEFAULT_DISTANCE_TRAVELLED);
        // Add required entity
        User createdBy = UserResourceIntTest.createEntity(em);
        em.persist(createdBy);
        em.flush();
        totem.setCreatedBy(createdBy);
        return totem;
    }

    @Before
    public void initTest() {
        totem = createEntity(em);
    }

    @Test
    @Transactional
    public void createTotem() throws Exception {
        int databaseSizeBeforeCreate = totemRepository.findAll().size();

        // Create the Totem
        TotemDTO totemDTO = totemMapper.totemToTotemDTO(totem);

        restTotemMockMvc.perform(post("/api/totems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(totemDTO)))
            .andExpect(status().isCreated());

        // Validate the Totem in the database
        List<Totem> totemList = totemRepository.findAll();
        assertThat(totemList).hasSize(databaseSizeBeforeCreate + 1);
        Totem testTotem = totemList.get(totemList.size() - 1);
        assertThat(testTotem.getCreationLatitude()).isEqualTo(DEFAULT_CREATION_LATITUDE);
        assertThat(testTotem.getCreationLongitude()).isEqualTo(DEFAULT_CREATION_LONGITUDE);
        assertThat(testTotem.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testTotem.getDistanceTravelled()).isEqualTo(DEFAULT_DISTANCE_TRAVELLED);
    }

    @Test
    @Transactional
    public void createTotemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = totemRepository.findAll().size();

        // Create the Totem with an existing ID
        Totem existingTotem = new Totem();
        existingTotem.setId(1L);
        TotemDTO existingTotemDTO = totemMapper.totemToTotemDTO(existingTotem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTotemMockMvc.perform(post("/api/totems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTotemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Totem> totemList = totemRepository.findAll();
        assertThat(totemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreationLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = totemRepository.findAll().size();
        // set the field null
        totem.setCreationLatitude(null);

        // Create the Totem, which fails.
        TotemDTO totemDTO = totemMapper.totemToTotemDTO(totem);

        restTotemMockMvc.perform(post("/api/totems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(totemDTO)))
            .andExpect(status().isBadRequest());

        List<Totem> totemList = totemRepository.findAll();
        assertThat(totemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = totemRepository.findAll().size();
        // set the field null
        totem.setCreationLongitude(null);

        // Create the Totem, which fails.
        TotemDTO totemDTO = totemMapper.totemToTotemDTO(totem);

        restTotemMockMvc.perform(post("/api/totems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(totemDTO)))
            .andExpect(status().isBadRequest());

        List<Totem> totemList = totemRepository.findAll();
        assertThat(totemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = totemRepository.findAll().size();
        // set the field null
        totem.setCreationDate(null);

        // Create the Totem, which fails.
        TotemDTO totemDTO = totemMapper.totemToTotemDTO(totem);

        restTotemMockMvc.perform(post("/api/totems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(totemDTO)))
            .andExpect(status().isBadRequest());

        List<Totem> totemList = totemRepository.findAll();
        assertThat(totemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDistanceTravelledIsRequired() throws Exception {
        int databaseSizeBeforeTest = totemRepository.findAll().size();
        // set the field null
        totem.setDistanceTravelled(null);

        // Create the Totem, which fails.
        TotemDTO totemDTO = totemMapper.totemToTotemDTO(totem);

        restTotemMockMvc.perform(post("/api/totems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(totemDTO)))
            .andExpect(status().isBadRequest());

        List<Totem> totemList = totemRepository.findAll();
        assertThat(totemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTotems() throws Exception {
        // Initialize the database
        totemRepository.saveAndFlush(totem);

        // Get all the totemList
        restTotemMockMvc.perform(get("/api/totems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(totem.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationLatitude").value(hasItem(DEFAULT_CREATION_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].creationLongitude").value(hasItem(DEFAULT_CREATION_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].distanceTravelled").value(hasItem(DEFAULT_DISTANCE_TRAVELLED.intValue())));
    }

    @Test
    @Transactional
    public void getTotem() throws Exception {
        // Initialize the database
        totemRepository.saveAndFlush(totem);

        // Get the totem
        restTotemMockMvc.perform(get("/api/totems/{id}", totem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(totem.getId().intValue()))
            .andExpect(jsonPath("$.creationLatitude").value(DEFAULT_CREATION_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.creationLongitude").value(DEFAULT_CREATION_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.distanceTravelled").value(DEFAULT_DISTANCE_TRAVELLED.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTotem() throws Exception {
        // Get the totem
        restTotemMockMvc.perform(get("/api/totems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTotem() throws Exception {
        // Initialize the database
        totemRepository.saveAndFlush(totem);
        int databaseSizeBeforeUpdate = totemRepository.findAll().size();

        // Update the totem
        Totem updatedTotem = totemRepository.findOne(totem.getId());
        updatedTotem
                .creationLatitude(UPDATED_CREATION_LATITUDE)
                .creationLongitude(UPDATED_CREATION_LONGITUDE)
                .creationDate(UPDATED_CREATION_DATE)
                .distanceTravelled(UPDATED_DISTANCE_TRAVELLED);
        TotemDTO totemDTO = totemMapper.totemToTotemDTO(updatedTotem);

        restTotemMockMvc.perform(put("/api/totems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(totemDTO)))
            .andExpect(status().isOk());

        // Validate the Totem in the database
        List<Totem> totemList = totemRepository.findAll();
        assertThat(totemList).hasSize(databaseSizeBeforeUpdate);
        Totem testTotem = totemList.get(totemList.size() - 1);
        assertThat(testTotem.getCreationLatitude()).isEqualTo(UPDATED_CREATION_LATITUDE);
        assertThat(testTotem.getCreationLongitude()).isEqualTo(UPDATED_CREATION_LONGITUDE);
        assertThat(testTotem.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTotem.getDistanceTravelled()).isEqualTo(UPDATED_DISTANCE_TRAVELLED);
    }

    @Test
    @Transactional
    public void updateNonExistingTotem() throws Exception {
        int databaseSizeBeforeUpdate = totemRepository.findAll().size();

        // Create the Totem
        TotemDTO totemDTO = totemMapper.totemToTotemDTO(totem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTotemMockMvc.perform(put("/api/totems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(totemDTO)))
            .andExpect(status().isCreated());

        // Validate the Totem in the database
        List<Totem> totemList = totemRepository.findAll();
        assertThat(totemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTotem() throws Exception {
        // Initialize the database
        totemRepository.saveAndFlush(totem);
        int databaseSizeBeforeDelete = totemRepository.findAll().size();

        // Get the totem
        restTotemMockMvc.perform(delete("/api/totems/{id}", totem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Totem> totemList = totemRepository.findAll();
        assertThat(totemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
