package root.bdd.justificatif;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface InterfaceJustificatifService {
	public List<Justificatif> findAll();
	public void addJustificatif();
	public Justificatif store (MultipartFile file);
	public Justificatif update (long justificatif_id, MultipartFile file);
	public void deleteById (long justificatif_id);
	public Justificatif getFile(long justificatif_id);
}
