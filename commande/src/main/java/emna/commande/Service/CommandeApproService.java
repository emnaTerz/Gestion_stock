package emna.commande.Service;

import emna.commande.dao.CommandeApproResponse;
import emna.commande.dao.CommandeApproRequest;

import java.util.List;

public interface CommandeApproService {
    CommandeApproResponse save(CommandeApproRequest request,String authorizationHeader);
    CommandeApproResponse getById(Integer id, String token);
    List<CommandeApproResponse> getAll();
    CommandeApproResponse update(Integer id, CommandeApproRequest request);
    void delete(Integer id);
}
