package pe.edu.upeu.boticaamanecer.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.boticaamanecer.modelo.Emisor;

@Repository
public interface EmisorRepository extends JpaRepository<Emisor, Long> {
}