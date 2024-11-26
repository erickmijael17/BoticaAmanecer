package pe.edu.upeu.boticaamanecer.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.boticaamanecer.dto.MenuMenuItenTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MenuMenuItemDao implements MenuMenuItenDaoI{

    @Override
    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma) {
        System.out.println("Aministrador");
        List<MenuMenuItenTO> lista = new ArrayList<>();
        lista.add(new MenuMenuItenTO(idioma.getProperty("menu.nombre.archivo"), "", "mifile"));
        lista.add(new MenuMenuItenTO(idioma.getProperty("menu.nombre.archivo"), "Salir", "misalir"));
        lista.add(new MenuMenuItenTO("Edit", "Cortar", "micut"));
        lista.add(new MenuMenuItenTO("Edit", "copiar", "micopy"));
        lista.add(new MenuMenuItenTO("Edit", "pegar", "mipaste"));
        lista.add(new MenuMenuItenTO("Edit", idioma.getProperty("menuitem.nombre.postulante"), "miselectall"));
        lista.add(new MenuMenuItenTO("Producto", "Reg. Producto", "miregproduct"));
        lista.add(new MenuMenuItenTO("Venta", "Reg. Venta", "miventa"));
        lista.add(new MenuMenuItenTO("Cliente", "Reg. Cliente", "micliente"));
        List<MenuMenuItenTO> accesoReal = new ArrayList<>();
        switch (perfil) {
            case "Administrador":
                accesoReal = lista;
                break;
            case "Vendedor":
                accesoReal.add(lista.get(0));
                accesoReal.add(lista.get(1));
                accesoReal.add(lista.get(7));
                break;
            case "Almacenero":
                accesoReal.add(lista.get(0));
                accesoReal.add(lista.get(1));
                accesoReal.add(lista.get(6));
                break;
            default:
                throw new AssertionError();
        }
        return accesoReal;
    }

}
