package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.live.LiveItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveItemRepository extends JpaRepository<LiveItem, String> {
    @Query("SELECT li FROM LiveItem li WHERE li.liveSessionId = ?1")
    List<LiveItem> findByLiveSessionId(String liveSessionId);
}
