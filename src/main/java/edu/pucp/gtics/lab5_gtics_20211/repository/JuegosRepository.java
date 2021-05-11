package edu.pucp.gtics.lab5_gtics_20211.repository;

import edu.pucp.gtics.lab5_gtics_20211.entity.Juegos;
import edu.pucp.gtics.lab5_gtics_20211.entity.JuegosUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface JuegosRepository extends JpaRepository<Juegos,Integer> {
     @Query(value = "SELECT ju.image as imageurl, ju.nombre as nombre, ju.descripcion as descripcion FROM gameshop3.juegos ju\n" +
             "inner join gameshop3.juegosxusuario jxu on (ju.idjuego = jxu.idjuego)\n" +
             "inner join gameshop3.usuarios us on (us.idusuario = jxu.idusuario)\n" +
             "where jxu.idusuario = ?", nativeQuery = true)
    List<JuegosUserDto> obtenerJuegosPorUser(int idusuario);
}
