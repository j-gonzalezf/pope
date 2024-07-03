package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TemplateDao;
import es.udc.fi.dc.tfg.model.entities.TemplateRowDao;
import es.udc.fi.dc.tfg.model.entities.TemplateRows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase TemplateServiceImpl.
 */
@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {

    /**
     * El template dao.
     */
    @Autowired
    private TemplateDao templateDao;

    /**
     * El template row dao.
     */
    @Autowired
    private TemplateRowDao templateRowDao;

    /**
     * Crea una nueva plantilla de entrenamiento
     *
     * @param template El objeto Templates que representa la plantilla a crear.
     */
    @Override
    public void createTemplate(Templates template) {
        templateDao.save(template);
    }

    /**
     * Añade una nueva fila a una plantilla de entrenamiento
     *
     * @param templateRow El objeto TemplateRows que representa la fila a añadir
     * a la plantilla.
     */
    @Override
    public void addTemplateRow(TemplateRows templateRow) {
        templateRowDao.save(templateRow);
    }

}
