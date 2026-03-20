package com.shop.pras.Repository;

import com.shop.pras.Models.Base;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository extends JpaRepository<Base, String> {
}