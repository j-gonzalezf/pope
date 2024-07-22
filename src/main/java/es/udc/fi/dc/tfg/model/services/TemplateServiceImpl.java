package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Exercises;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TemplateDao;
import es.udc.fi.dc.tfg.model.entities.TemplateRowDao;
import es.udc.fi.dc.tfg.model.entities.TemplateRows;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

    /**
     * Devuelve una lista con las plantillas de un ciclo.
     *
     * @param cycleId El ID del ciclo.
     * @return La lista de objetos Templates que representa las plantillas.
     */
    @Override
    public List<Templates> getTemplates(Long cycleId) {

        List<Templates> templates = templateDao.findTemplates(cycleId);
        return templates;

    }

    /**
     * Devuelve una lista con las filas de una plantilla.
     *
     * @param templateId El ID de la plantilla.
     * @return La lista de objetos TemplateRows que representa las filas.
     */
    @Override
    public List<TemplateRows> getTemplateRows(Long templateId) {

        List<TemplateRows> templateRows = templateRowDao.findByTemplateId(templateId);
        return templateRows;

    }

    /**
     * Devuelve una plantilla a partir de su ID.
     *
     * @param templateId El ID de la plantilla.
     * @return El objeto Templates que representa la plantilla solicitada.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    @Override
    public Templates getTemplateInfo(Long templateId) throws InstanceNotFoundException {

        Optional<Templates> Otemplate = templateDao.findById(templateId);

        if (!Otemplate.isPresent()) {
            throw new InstanceNotFoundException("project.entities.templates", templateId);
        }

        Templates template = Otemplate.get();

        return template;

    }

    /**
     * Edita una plantilla.
     *
     * @param id El ID de la plantilla.
     * @param name El nombre de la plantilla.
     * @return El objeto Templates que representa la plantilla actualizada.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    @Override
    public Templates updateTemplate(Long id, String name)
            throws InstanceNotFoundException {

        Optional<Templates> Otemplate = templateDao.findById(id);

        if (!Otemplate.isPresent()) {
            throw new InstanceNotFoundException("project.entities.templates", id);
        }

        Templates template = Otemplate.get();

        template.setName(name);

        return template;

    }

    /**
     * Edita la fila de una plantilla.
     *
     * @param id El ID de la fila.
     * @param series Las series a realizar del ejercicio.
     * @param reps Las repeticiones a realizar del ejercicio.
     * @param weight El peso a levantar en el ejercicio.
     * @param exercise El ejercicio a realizar.
     * @return El objeto TemplateRows que representa la fila actualizada.
     * @throws InstanceNotFoundException si no se encuentra ninguna fila.
     */
    @Override
    public TemplateRows updateTemplateRow(Long id, Integer series, Integer reps,
            BigDecimal weight, Exercises exercise) throws InstanceNotFoundException {

        Optional<TemplateRows> OtemplateRow = templateRowDao.findById(id);

        if (!OtemplateRow.isPresent()) {
            throw new InstanceNotFoundException("project.entities.templateRows", id);
        }

        TemplateRows row = OtemplateRow.get();

        row.setSeries(series);
        row.setRepetitions(reps);
        row.setWeight(weight);
        row.setExercise(exercise);

        return row;

    }

    /**
     * Elimina una plantilla.
     *
     * @param id El ID de la plantilla.
     * @return El ID de la plantilla que ha sido eliminada.
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado.
     */
    @Override
    public Long deleteTemplate(Long id) throws InstanceNotFoundException {

        Optional<Templates> Otemplate = templateDao.findById(id);

        if (!Otemplate.isPresent()) {
            throw new InstanceNotFoundException("project.entities.templates", id);
        }

        templateDao.deleteById(id);

        return id;

    }

    /**
     * Elimina la fila de una plantilla.
     *
     * @param id El ID de la fila.
     * @return El ID de la fila que ha sido eliminada.
     * @throws InstanceNotFoundException si no se encuentra una fila con el ID
     * proporcionado.
     */
    @Override
    public Long deleteTemplateRow(Long id) throws InstanceNotFoundException {

        Optional<TemplateRows> OtemplateRow = templateRowDao.findById(id);

        if (!OtemplateRow.isPresent()) {
            throw new InstanceNotFoundException("project.entities.templateRows", id);
        }

        templateRowDao.deleteById(id);

        return id;

    }

}
