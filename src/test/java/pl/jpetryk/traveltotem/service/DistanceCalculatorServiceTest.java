package pl.jpetryk.traveltotem.service;

import org.assertj.core.data.Percentage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.jpetryk.traveltotem.TraveltotemApp;
import pl.jpetryk.traveltotem.domain.Totem;
import pl.jpetryk.traveltotem.domain.Transfer;
import pl.jpetryk.traveltotem.repository.TotemRepository;
import pl.jpetryk.traveltotem.repository.TransferRepository;
import pl.jpetryk.traveltotem.web.rest.TotemResourceIntTest;
import pl.jpetryk.traveltotem.web.rest.TransferResourceIntTest;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by jpetryk on 14.02.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TraveltotemApp.class)
@Transactional
public class DistanceCalculatorServiceTest {


    @Inject
    private EntityManager em;

    @Autowired
    private TotemRepository totemRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private DistanceCalculatorService distanceCalculatorService;

    @Test
    public void calculateDistanceWithoutTransfers() throws Exception {
        Totem totemWithoutTransfers = TotemResourceIntTest.createEntity(em);
        totemWithoutTransfers = totemRepository.save(totemWithoutTransfers);
        assertThat(distanceCalculatorService.calculateDistance(totemWithoutTransfers.getId())).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void calculateDistanceWithOneTransfer() throws Exception {
        Totem totemWithOneTransfer = prepareTotem(51.109558, 17.03207); // wroclaw city hall
        Transfer transfer = prepareTransfer(totemWithOneTransfer, 50.717561, 23.252542); // zamosc city hall
        totemWithOneTransfer = totemRepository.save(totemWithOneTransfer);
        transferRepository.save(transfer);
        assertThat(distanceCalculatorService.calculateDistance(totemWithOneTransfer.getId())).isCloseTo(BigDecimal.valueOf(438), Percentage.withPercentage(1));
    }

    @Test
    public void calculateDistanceWithMultipleTransfers() throws Exception {
        Totem totemWithMultipleTransfers = prepareTotem(51.109558, 17.03207); // wroclaw city hall
        totemWithMultipleTransfers = totemRepository.save(totemWithMultipleTransfers);
        transferRepository.save(prepareTransfer(totemWithMultipleTransfers, 50.717561, 23.252542));
        transferRepository.save(prepareTransfer(totemWithMultipleTransfers, 52.2398671, 20.9977881));
        transferRepository.save(prepareTransfer(totemWithMultipleTransfers, 50.0605377, 19.9375606));
        assertThat(distanceCalculatorService.calculateDistance(totemWithMultipleTransfers.getId())).isCloseTo(BigDecimal.valueOf(921), Percentage.withPercentage(1));
    }

    private Transfer prepareTransfer(Totem totemWithOneTransfer, double latitude, double longitude) {
        Transfer transfer = TransferResourceIntTest.createEntity(em);
        transfer.setLatitude(latitude);
        transfer.setLongitude(longitude);
        transfer.setTotem(totemWithOneTransfer);
        return transfer;
    }

    private Totem prepareTotem(double latitude, double longitude) {
        Totem totemWithOneTransfer = TotemResourceIntTest.createEntity(em);
        totemWithOneTransfer.setCreationLatitude(latitude);
        totemWithOneTransfer.setCreationLongitude(longitude);
        return totemWithOneTransfer;
    }


}
