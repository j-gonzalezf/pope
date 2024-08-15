package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.SensationDao;
import es.udc.fi.dc.tfg.model.entities.Sensations;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase SensationServiceImpl.
 */
@Service
@Transactional
public class SensationServiceImpl implements SensationService {

    /**
     * El weight dao.
     */
    @Autowired
    private SensationDao sensationDao;

    /**
     * Crea un nuevo registro de sensaciones.
     *
     * @param sensations el objeto Sensations que representa las sensaciones a
     * registrar.
     */
    @Override
    public void sensationsRegister(Sensations sensations) {

        sensationDao.save(sensations);

    }

    /**
     * Devuelve un registro de sensaciones a partir del ID de su plantilla
     *
     * @param templateId El ID de la plantilla.
     * @return El objeto Sensations que representa el registro solicitado.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    @Override
    public Sensations getSensationInfo(Long templateId) throws InstanceNotFoundException {

        Optional<Sensations> Osensation = sensationDao.findByTemplateId(templateId);

        if (!Osensation.isPresent()) {
            throw new InstanceNotFoundException("project.entities.templates", templateId);
        }

        Sensations sensation = Osensation.get();

        return sensation;

    }

    /**
     * Edita un registro de sensaciones.
     *
     * @param id El ID del registro.
     * @param fatigue La fatiga registrada.
     * @param stiffness Las agujetas registradas.
     * @param motivation La motivación registrada.
     * @param sleep La calidad de sueño registrada.
     * @return El objeto Sensations que representa el registro actualizado.
     * @throws InstanceNotFoundException si no se encuentra ningún registro.
     */
    @Override
    public Sensations updateSensations(Long id, Integer fatigue, Integer stiffness,
            Integer motivation, Integer sleep) throws InstanceNotFoundException {

        Optional<Sensations> Osensation = sensationDao.findById(id);

        if (!Osensation.isPresent()) {
            throw new InstanceNotFoundException("project.entities.sensations", id);
        }

        Sensations sensations = Osensation.get();

        sensations.setFatigue(fatigue);
        sensations.setStiffness(stiffness);
        sensations.setMotivation(motivation);
        sensations.setSleep(sleep);

        return sensations;

    }

}
