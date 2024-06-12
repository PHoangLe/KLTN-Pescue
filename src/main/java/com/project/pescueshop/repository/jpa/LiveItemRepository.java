package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.live.LiveItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveItemRepository extends JpaRepository<LiveItem, String> {
}
