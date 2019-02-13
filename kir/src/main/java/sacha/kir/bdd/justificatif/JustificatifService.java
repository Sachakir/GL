package sacha.kir.bdd.justificatif;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
	
	public Justificatif storeJustificatif (MultipartFile file) {
		String filepath = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			if(filepath.contains("..")) {
                System.err.println("Sorry! Filename contains invalid path sequence " + filepath);
            }

            Justificatif j = new Justificatif((long)repository.getMaxId() + 1, file.getBytes());

            return repository.save(j);
        } catch (IOException e) {
            System.err.println("Could not store file " + filepath + ". Please try again!");
            e.printStackTrace();
            return null;
        }
	}
	
	public Justificatif getFile(Long justificatif_id) {
		return repository.findById(justificatif_id).orElse(null);
	}
}
