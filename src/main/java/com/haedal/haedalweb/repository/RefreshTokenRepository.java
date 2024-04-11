package com.haedal.haedalweb.repository;

import com.haedal.haedalweb.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
