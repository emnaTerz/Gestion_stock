package emna.commande.Repository;

import emna.commande.Entities.CommandeAppro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeApproRepository extends JpaRepository<CommandeAppro, Integer> {
}