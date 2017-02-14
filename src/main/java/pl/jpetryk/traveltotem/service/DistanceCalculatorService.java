package pl.jpetryk.traveltotem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jpetryk.traveltotem.domain.Totem;
import pl.jpetryk.traveltotem.domain.Transfer;
import pl.jpetryk.traveltotem.repository.TotemRepository;
import pl.jpetryk.traveltotem.repository.TransferRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jpetryk on 14.02.2017.
 */
@Service
public class DistanceCalculatorService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TotemRepository totemRepository;

    public BigDecimal calculateDistance(Long totemId) {
        List<Transfer> totemTransfers = transferRepository.findByTotemIdOrderByIdAsc(totemId);
        if (totemTransfers.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            Totem totem = totemRepository.findOne(totemId);
            if (totem == null) {
                return BigDecimal.ZERO;
            } else {
                Transfer referenceTransfer = totemTransfers.get(0);
                BigDecimal result = distance(totem.getCreationLatitude(), totem.getCreationLongitude(), referenceTransfer.getLatitude(), referenceTransfer.getLongitude());
                for (Transfer transfer : totemTransfers.subList(1, totemTransfers.size())) {
                    result = result.add(distance(referenceTransfer.getLatitude(), referenceTransfer.getLongitude(), transfer.getLatitude(), transfer.getLongitude()));
                    referenceTransfer = transfer;
                }
                return result;
            }
        }
    }

    private static BigDecimal distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; //kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (earthRadius * c);
        return BigDecimal.valueOf(dist).setScale(2, BigDecimal.ROUND_FLOOR);
    }
}
