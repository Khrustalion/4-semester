package org.khrustalev.listener;

import java.util.List;

import org.khrustalev.config.RabbitConfig;
import org.khrustalev.dto.OwnerDto;
import org.khrustalev.dto.Page.PageDto;
import org.khrustalev.dto.Page.PageResponseDto;
import org.khrustalev.dto.Page.PageResponseMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.khrustalev.service.OwnerService;

@Component
public class OwnerListener {
    private final OwnerService ownerService;

    public OwnerListener(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @RabbitListener(queues = RabbitConfig.OWNER_SAVE_QUEUE)
    public OwnerDto save(OwnerDto dto) {
        return ownerService.save(dto);
    }

    @RabbitListener(queues = RabbitConfig.OWNER_UPDATE_QUEUE)
    public OwnerDto update(OwnerDto dto) {
        return ownerService.update(dto);
    }

    @RabbitListener(queues = RabbitConfig.OWNER_GET_QUEUE)
    public OwnerDto findById(long ownerId) {
        return ownerService.findById(ownerId);
    }

    @RabbitListener(queues = RabbitConfig.OWNER_LIST_QUEUE)
    public List<OwnerDto> findAll(Void ignored) {
        return ownerService.findAll();
    }

    @RabbitListener(queues = RabbitConfig.OWNER_PAGE_QUEUE)
    public PageResponseDto<OwnerDto> findPage(PageDto pageReq) {
        return ownerService.findAll(
                org.springframework.data.domain.PageRequest.of(
                        pageReq.getPage(),
                        pageReq.getSize(),
                        pageReq.isDescending()
                                ? org.springframework.data.domain.Sort.by(pageReq.getSortBy()).descending()
                                : org.springframework.data.domain.Sort.by(pageReq.getSortBy()).ascending()
                )
        );
    }

    @RabbitListener(queues = RabbitConfig.OWNER_DELETE_QUEUE)
    public OwnerDto deleteById(OwnerDto req) {
        return ownerService.deleteById(req.getId());
    }
}
