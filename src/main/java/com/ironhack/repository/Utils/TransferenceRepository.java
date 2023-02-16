package com.ironhack.repository.Utils;

import com.ironhack.model.Utils.Transference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenceRepository extends JpaRepository<Transference, Long> {
    Transference findTransferenceById(Long id);
}