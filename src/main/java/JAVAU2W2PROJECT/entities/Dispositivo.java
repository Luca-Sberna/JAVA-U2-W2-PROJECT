package JAVAU2W2PROJECT.entities;

import JAVAU2W2PROJECT.utils.StatoDispositivo;
import JAVAU2W2PROJECT.utils.TipoDispositivo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Dispositivo {
	@Id
	@GeneratedValue
	private TipoDispositivo tipoDispositivo;
	private StatoDispositivo statoDispositivo;

}
