package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.WeightDao;
import es.udc.fi.dc.tfg.model.entities.Weights;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase UserExtraServiceImpl.
 */
@Service
@Transactional
public class UserExtraServiceImpl implements UserExtraService {

    /**
     * El weight dao.
     */
    @Autowired
    private WeightDao weightDao;

    /**
     * Crea un nuevo registro de peso.
     *
     * @param weight el objeto Weights que representa el peso a registrar.
     */
    @Override
    public void weightRegister(Weights weight) {

        weightDao.save(weight);

    }

    /**
     * Devuelve el último peso registrado de un cliente.
     *
     * @param clientId El ID del cliente.
     * @return El objeto Weights que representa el peso.
     */
    @Override
    public Weights getLastWeight(Long clientId) {

        List<Weights> weights = weightDao.findByClientId(clientId);

        return weights.isEmpty() ? null : weights.get(0);

    }

    /**
     * Devuelve los pesos registrados de un cliente.
     *
     * @param clientId El ID del cliente.
     * @return El objeto Weights que representa el peso.
     */
    @Override
    public List<Weights> getWeights(Long clientId) {

        List<Weights> weights = weightDao.findByClientId(clientId);

        return weights;

    }

}
