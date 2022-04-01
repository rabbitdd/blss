package main.service;

import main.entity.Page;
import main.repository.PageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageService {

    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public Long getPageByName(String name){
        Optional<Page> page = pageRepository.getPageByName(name);
        if(page.isPresent()){
            Page p = page.get();
            return p.getId();
        }
        return (long) -1;
    }

    public Page getPageById(Long id){
        return pageRepository.getPageById(id);
    }

    public List<Page> getAll(){
        return pageRepository.getAllBy();
    }
}
