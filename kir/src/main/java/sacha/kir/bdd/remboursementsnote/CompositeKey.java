package sacha.kir.bdd.remboursementsnote;

import java.io.Serializable;

public class CompositeKey implements Serializable
{
	private static final long serialVersionUID = 2957379385507367084L;
	
	private Long note_id;
	private Long demande_id;
}
