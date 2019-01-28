package sacha.kir.bdd.conges;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CongesService implements InterfaceCongesService
{
	@Autowired
    private	CongesRepository repository;
	
	@Override
	public List<Conges> findAll() {
		List<Conges> users = (List<Conges>) repository.findAll();
		return users;
	}

	@Override
	public void addConges() 
	{
		Conges c = new Conges();
		c.setCongesid((long) 41);
		c.setDatedebut("2019-01-22");
		c.setDatefin("2019-01-23");
		c.setUid(9);
		c.setValidationchefdeservice("EnAttente");
		c.setValidationrh("EnAttente");
		repository.save(c);
	}

}
