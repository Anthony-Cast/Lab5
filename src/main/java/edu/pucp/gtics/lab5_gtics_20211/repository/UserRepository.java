package edu.pucp.gtics.lab5_gtics_20211.repository;

import edu.pucp.gtics.lab5_gtics_20211.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value="select * from usuarios where correo=?1",nativeQuery = true)
    User obtenerUsuario(String correo);

}
