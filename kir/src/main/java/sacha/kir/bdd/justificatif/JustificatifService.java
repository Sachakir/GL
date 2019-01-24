package sacha.kir.bdd.justificatif;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JustificatifService implements InterfaceJustificatifService {

	@Autowired
	JustificatifRepository repository;
	
	@Override
	public List<Justificatif> findAll() {
		List<Justificatif> users = (List<Justificatif>) repository.findAll();
		return users;
	}

	@Override
	public void addJustificatif() {
		Justificatif j = new Justificatif();
		j.setJustificatif_id((long) 3);
		byte[] tab = new byte[2];
		tab[0] = 1;
		tab[1] = 2;
		j.setPdf(tab);
		
		repository.save(j);
		
	}

}
