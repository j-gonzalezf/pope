package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.SensationDao;
import es.udc.fi.dc.tfg.model.entities.Sensations;
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

}
