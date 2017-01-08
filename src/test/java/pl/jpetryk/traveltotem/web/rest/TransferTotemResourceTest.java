package pl.jpetryk.traveltotem.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jpetryk.traveltotem.TraveltotemApp;
import pl.jpetryk.traveltotem.service.dto.TransferDTO;
import pl.jpetryk.traveltotem.domain.Totem;
import pl.jpetryk.traveltotem.domain.Transfer;
import pl.jpetryk.traveltotem.domain.User;
import pl.jpetryk.traveltotem.domain.enumeration.TransferStatus;
import pl.jpetryk.traveltotem.repository.TotemRepository;
import pl.jpetryk.traveltotem.repository.TransferRepository;
import pl.jpetryk.traveltotem.repository.UserRepository;
import pl.jpetryk.traveltotem.service.TransferService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jpetryk on 10.10.2016.
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = TraveltotemApp.class)
@Sql({"/sendAndReceiveTestData.sql"})
public class TransferTotemResourceTest {

    public static final double LATITUDE = 20.0;
    public static final double LONGITUDE = 20.0;
    public static final TransferStatus STATUS = TransferStatus.NEW;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private TransferService transferService;

    @Inject
    private TransferRepository transferRepository;

    @Inject
    private TotemRepository totemRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc mockMvc;

    private Totem totem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransferTotemResource transferTotemResource = new TransferTotemResource();
        ReflectionTestUtils.setField(transferTotemResource, "transferService", transferService);
        mockMvc = MockMvcBuilders.standaloneSetup(transferTotemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        totem = totemRepository.findAll().get(0);
    }

    @Test
    @Transactional
    public void sendAndReceive() throws Exception {
        int databaseSizeBeforeCreate = transferRepository.findAll().size();
        User fromUser = userRepository.findOneByLogin("from").get();
        User toUser = userRepository.findOneByLogin("to").get();
        TransferDTO transferDTO = transferDTO(fromUser.getId(), totem.getId());
        MvcResult send = mockMvc.perform(post("/api/totems/send")
            .content(TestUtil.convertObjectToJsonBytes(transferDTO))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(request().asyncStarted())
            .andReturn();
        transferDTO.setToUserId(toUser.getId());
        transferDTO.setFromUserId(null);
        MvcResult receive = mockMvc.perform(post("/api/totems/receive")
            .content(TestUtil.convertObjectToJsonBytes(transferDTO))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(request().asyncStarted())
            .andReturn();
        send.getAsyncResult();

        receive.getAsyncResult();
        mockMvc.perform(asyncDispatch(send)).andExpect(status().isOk());
        mockMvc.perform(asyncDispatch(receive)).andExpect(status().isOk());
        List<Transfer> transfers = transferRepository.findAll();
        assertThat(transfers).hasSize(databaseSizeBeforeCreate + 1);
        Transfer result = transfers.get(transfers.size() - 1);
        assertThat(result).isNotNull();
        assertThat(result.getLatitude()).isEqualTo(LATITUDE);
        assertThat(result.getLongitude()).isEqualTo(LONGITUDE);
        assertThat(result.getFromUser().getId()).isEqualTo(fromUser.getId());
        assertThat(result.getToUser().getId()).isEqualTo(toUser.getId());
        assertThat(result.getTotem().getId()).isEqualTo(totem.getId());

    }

    private TransferDTO transferDTO(Long userFromId, Long totemId) {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setFromUserId(userFromId);
        transferDTO.setTotemId(totemId);
        transferDTO.setDate(LocalDate.now());
        transferDTO.setLatitude(LATITUDE);
        transferDTO.setLongitude(LONGITUDE);
        transferDTO.setStatus(STATUS);
        return transferDTO;
    }

}
