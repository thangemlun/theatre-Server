package remote;

import lombok.Data;
import model.CinemaMM;

import java.util.List;

@Data
public class ZaloCinema {
    private String name;
    private List<CinemaMM> cinemas;
}
