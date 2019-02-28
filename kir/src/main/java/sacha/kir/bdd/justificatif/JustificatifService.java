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
	
	@Override
	public Justificatif store (MultipartFile file) {
		String filepath = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if(filepath.contains("..")) {
                System.err.println("Sorry! Filename contains invalid path sequence " + filepath);
            }
			
			int justificatif_id = 1;
			if(repository.count() > 0) {
				justificatif_id += repository.getMaxId();
			}
				
			Justificatif j = new Justificatif(justificatif_id, file.getBytes(), file.getOriginalFilename());
            return repository.save(j);
            
        } catch (IOException e) {
            System.err.println("Could not store file " + filepath + ". Please try again!");
            e.printStackTrace();
            return null;
        }
	}
	
	@Override
	public Justificatif update (long justificatif_id, MultipartFile file) {
		String filepath = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			Justificatif j = new Justificatif(justificatif_id, file.getBytes(), file.getOriginalFilename());
	        return repository.save(j);
		} catch (IOException e) {
            System.err.println("Could not store file " + filepath + ". Please try again!");
            e.printStackTrace();
            return null;
        }
	}
	
	@Override
	public void deleteById (long justificatif_id) {
		repository.deleteById(justificatif_id);
	}
	
	@Override
	public Justificatif getFile (long justificatif_id) {
		return repository.findById(justificatif_id).orElse(null);
	}
}
