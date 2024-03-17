package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.GlobalSearchResultDTO;
import com.project.pescueshop.repository.dao.SearchingDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchingService {
    private final SearchingDAO searchingDAO;

    public Map<String, List<GlobalSearchResultDTO>> globalSearch(String keyword){
        List<GlobalSearchResultDTO> resultList = searchingDAO.globalSearch(keyword);

        return resultList.stream()
                .collect(Collectors.groupingBy(GlobalSearchResultDTO::getGroupName));
    }
}
