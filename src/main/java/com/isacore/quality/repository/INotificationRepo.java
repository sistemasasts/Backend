package com.isacore.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.Notification;


@Repository
public interface INotificationRepo extends JpaRepository<Notification, String> {
	
	
	@Query(value = "select * from notification  where noti_user= :idUser and noti_state= :state", nativeQuery = true)
	List<Notification> findByIdUserAndState(@Param("idUser") String idUser, @Param("state") String state);
	
	@Query(value = "select * from notification  where noti_idprocess= :idProcess ", nativeQuery = true)
	List<Notification> findByIdProcess(@Param("idProcess") Integer idProcess);
	
	@Query(value = "select * from notification  where noti_idprocess= :idProcess and noti_state= :state ", nativeQuery = true)
	List<Notification> findByIdProcessAndState(@Param("idProcess") Integer idProcess, @Param("state") String state);
	
}
