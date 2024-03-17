package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.dto.GlobalSearchResultDTO;
import com.project.pescueshop.repository.mapper.GlobalSearchResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchingDAO extends BaseDAO{
    private final GlobalSearchResultMapper globalSearchResultMapper;

    public List<GlobalSearchResultDTO> globalSearch(String keyword){
        String sql = "SELECT * FROM global_search(:p_key_word);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_key_word", keyword);

        return jdbcTemplate.query(sql, parameters, globalSearchResultMapper);
    }
}
