package pl.jpetryk.traveltotem.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import pl.jpetryk.traveltotem.service.dto.TransferDTO;
import pl.jpetryk.traveltotem.service.TransferService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jpetryk on 09.10.2016.
 */
@RestController
@RequestMapping("/api")
public class TransferTotemResource {

    private final Logger log = LoggerFactory.getLogger(TransferTotemResource.class);

    @Inject
    private TransferService transferService;

    private Map<Long, TransferDTO> completedTotemsById = new ConcurrentHashMap<>();

    private Map<Long, TransferDTO> waitingTransfersByTotemId = new ConcurrentHashMap();

    public static final long TIMEOUT = 5000;

    @ResponseBody
    @RequestMapping(value = "/totems/send", method = RequestMethod.POST)
    public DeferredResult<TransferDTO> sendTransfer(@Valid @RequestBody TransferDTO transferDTO) {
        DeferredResult<TransferDTO> deferredResult = new DeferredResult(TIMEOUT);
        Long totemId = transferDTO.getTotemId();
        CompletableFuture.supplyAsync(() -> {
            TransferDTO result = null;
            if (waitingTransfersByTotemId.containsKey(totemId)) {
                result = waitingTransfersByTotemId.get(totemId);
                result.setFromUserId(transferDTO.getFromUserId());
                result = transferService.save(result);
                completedTotemsById.put(totemId, result);
                waitingTransfersByTotemId.remove(totemId);
            } else {
                result = returnTransferWhenOtherRequestIsCompleted(totemId, transferDTO);
            }
            return result;
        }).whenCompleteAsync((result, throwable) -> {
                deferredResult.setResult(result);
            }
        );
        deferredResult.onTimeout(() -> {
            completedTotemsById.remove(totemId);
            waitingTransfersByTotemId.remove(totemId);
            deferredResult.setErrorResult(new ResponseEntity(HttpStatus.BAD_REQUEST));

        });
        return deferredResult;
    }

    @ResponseBody
    @RequestMapping(value = "/totems/receive", method = RequestMethod.POST)
    public DeferredResult<TransferDTO> receiveTransfer(@Valid @RequestBody TransferDTO transferDTO) {
        DeferredResult<TransferDTO> deferredResult = new DeferredResult(TIMEOUT);
        Long totemId = transferDTO.getTotemId();
        CompletableFuture.supplyAsync(() -> {
            TransferDTO result = null;
            if (waitingTransfersByTotemId.containsKey(totemId)) {
                result = waitingTransfersByTotemId.get(totemId);
                result.setToUserId(transferDTO.getToUserId());
                result = transferService.save(result);
                completedTotemsById.put(totemId, result);
                waitingTransfersByTotemId.remove(totemId);
            } else {
                result = returnTransferWhenOtherRequestIsCompleted(totemId, transferDTO);
            }
            return result;
        }).whenCompleteAsync((result, throwable) -> {
                deferredResult.setResult(result);
            }
        );
        deferredResult.onTimeout(() -> {
            completedTotemsById.remove(totemId);
            waitingTransfersByTotemId.remove(totemId);
            deferredResult.setErrorResult(new ResponseEntity(HttpStatus.BAD_REQUEST));
        });
        return deferredResult;
    }

    private TransferDTO returnTransferWhenOtherRequestIsCompleted(long totemId, TransferDTO transferDTO) {
        long start = System.currentTimeMillis();
        waitingTransfersByTotemId.put(totemId, transferDTO);
        boolean isTimedOut = false;
        //release only when created instance is put in map
        while (!completedTotemsById.containsKey(totemId) && !isTimedOut) {
            try {
                Thread.sleep(50l);
                log.info("Waiting for other request. Transfer id: " + totemId);
                isTimedOut = System.currentTimeMillis() - start > TIMEOUT;
            } catch (InterruptedException e) {
                log.error("Interupted", e);
            }
        }
        TransferDTO result = completedTotemsById.get(totemId);
        completedTotemsById.remove(totemId);
        waitingTransfersByTotemId.remove(totemId);
        return result;
    }


}
