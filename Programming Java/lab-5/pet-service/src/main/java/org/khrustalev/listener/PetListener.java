package org.khrustalev.listener;

import java.util.List;

import lombok.AllArgsConstructor;
import org.khrustalev.config.RabbitConfig;
import org.khrustalev.dto.AddFriendDto;
import org.khrustalev.dto.Page.PageDto;
import org.khrustalev.dto.Page.PageResponseDto;
import org.khrustalev.dto.PetDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import org.khrustalev.service.PetService;

@Component
public class PetListener {
    private PetService petService;

    @Autowired
    public PetListener(PetService petService) {
        this.petService = petService;
    }

    @RabbitListener(queues = RabbitConfig.PET_SAVE_QUEUE)
    public PetDto save(PetDto dto) {
        return petService.save(dto);
    }

    @RabbitListener(queues = RabbitConfig.PET_UPDATE_QUEUE)
    public PetDto update(PetDto dto) {
        return petService.update(dto);
    }

    @RabbitListener(queues = RabbitConfig.PET_GET_QUEUE)
    public PetDto findById(PetDto req) {
        return petService.findById(req.getId());
    }

    @RabbitListener(queues = RabbitConfig.PET_DELETE_QUEUE)
    public PetDto deleteById(PetDto req) {
        return petService.deleteById(req.getId());
    }

    @RabbitListener(queues = RabbitConfig.PET_LIST_QUEUE)
    public List<PetDto> findAll() {
        return petService.findAll();
    }

    @RabbitListener(queues = RabbitConfig.PET_PAGE_QUEUE)
    public PageResponseDto<PetDto> findPage(PageDto pageReq) {
        return petService.findAll(
                org.springframework.data.domain.PageRequest.of(
                        pageReq.getPage(),
                        pageReq.getSize(),
                        pageReq.isDescending()
                                ? org.springframework.data.domain.Sort.by(pageReq.getSortBy()).descending()
                                : org.springframework.data.domain.Sort.by(pageReq.getSortBy()).ascending()
                )
        );
    }

    @RabbitListener(queues = RabbitConfig.PET_ADD_FRIEND_QUEUE)
    public PetDto addFriend(AddFriendDto dto) {
        return petService.addFriend(dto.getPet().getId(), dto.getFriend().getId());
    }
}
