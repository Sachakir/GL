package sacha.kir.bdd.justificatif;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface InterfaceJustificatifService {
	public List<Justificatif> findAll();
	public void addJustificatif();
	public Justificatif storeJustificatif (MultipartFile file);
	public Justificatif getFile(Long justificatif_id);
}
