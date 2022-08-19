package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.model.QualityCertificate;
import com.isacore.quality.model.QualityCertificatePK;

@Repository
public interface IQualityCertificateRepo extends JpaRepository<QualityCertificate, QualityCertificatePK> {

	@Query(value = "select qc.qc_count from quality_certificate qc where qc.hcc_head_id = :idHcc and qc.client_imptek_cli_id = :idC", nativeQuery = true)
	Integer findCertificateByPK(@Param("idHcc") long idHcc, @Param("idC") Integer idC);

	@Transactional
	@Modifying
	@Query(value = "insert into quality_certificate (hcc_head_id, client_imptek_cli_id, qc_email, qc_count, qc_client_print) values (:idHcc, :idC, :emailC, :countC, :clientPrint)", nativeQuery = true)
	void createCertificate(@Param("idHcc") long idHcc, @Param("idC") Integer idC, @Param("emailC") String emailC,
			@Param("countC") Integer countC, @Param("clientPrint") String clientPrint);

	@Transactional
	@Modifying
	@Query(value = "update quality_certificate set qc_count = :countC, qc_client_print= :clientPrint where hcc_head_id = :idHcc and client_imptek_cli_id = :idC", nativeQuery = true)
	void updateCount(@Param("countC") Integer countC, @Param("idHcc") long idHcc, @Param("idC") Integer idC, @Param("clientPrint") String clientPrint );
}
