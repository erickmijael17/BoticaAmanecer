package pe.edu.upeu.boticaamanecer.servicio;


import pe.edu.upeu.boticaamanecer.dto.MenuMenuItenTO;

import java.util.List;
import java.util.Properties;

public interface MenuMenuItenDaoI {

    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma);

}
