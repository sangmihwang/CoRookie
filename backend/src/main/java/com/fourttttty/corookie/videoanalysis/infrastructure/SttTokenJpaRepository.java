package com.fourttttty.corookie.videoanalysis.infrastructure;

import com.fourttttty.corookie.videoanalysis.domain.SttToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SttTokenJpaRepository extends JpaRepository<SttToken, Long> {
}
