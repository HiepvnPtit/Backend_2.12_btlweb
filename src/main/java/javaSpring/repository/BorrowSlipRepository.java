package javaSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javaSpring.entity.BorrowSlip;

@Repository
public interface BorrowSlipRepository extends JpaRepository<BorrowSlip, Long> {
    boolean existsBySlipCode(String slipCode);
}
