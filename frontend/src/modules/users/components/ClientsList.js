import Col from 'react-bootstrap/Col';
import Image from 'react-bootstrap/Image';
import Row from 'react-bootstrap/Row';
import { BsFillPlusCircleFill } from "react-icons/bs";
import './ClientsList.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Errors } from '../../common';
import DefaultIcon from '../../common/images/client-logo.webp';
import * as actions from '../actions';
import * as selectors from '../selectors';

const ClientsList = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [error, setError] = useState(null);

    const user = useSelector(selectors.getUser);
    const getClients = useSelector(selectors.getClients);

    const redirectToCreateClient = () => {
        navigate('/users/addClient');
    };

    const redirectToClientDetails = (clientId) => {
        navigate(`/users/clientDetails/${clientId}`);
    };

    useEffect(() => {

        dispatch(actions.getClients(user.id,
            () => { },
            errors => setError(errors)));

    }, [dispatch, user.id]);

    useEffect(() => {
        // Este useEffect se encarga de aplicar la clase 'desbordado' si el texto es más largo que el contenedor
        const clientNames = document.querySelectorAll('.textoDifuminado');

        clientNames.forEach(name => {
            if (name.scrollWidth > name.clientWidth) {
                name.classList.add('desbordado');
            }
        });
    }, [getClients]); // Se ejecuta cada vez que getClients se actualice

    return (

        <div fluid="true" className='ClientsList'>

            <h3 className="title">
                <FormattedMessage id="project.users.clients.title" />
            </h3>

            <Row className="listStyle">

                {getClients && (getClients.map((client) => (

                    <Col xs={12} sm={6} md={4} lg={3} key={client.id} className="listColStyle" >

                        <button className="listItemStyle" onClick={() => redirectToClientDetails(client.id)}>
                            <div className="image-container">
                                {client.icon ? (client.icon.map((icon, index) => (
                                    <Image
                                        key={index}
                                        src={"data:image/png;base64," + icon.base64}
                                        alt={icon.name}
                                        className="smallImageStyle"
                                    />
                                )))
                                    : (
                                        <Image
                                            src={DefaultIcon}
                                            alt="Default Icon"
                                            className="smallImageStyle"
                                        />
                                    )}
                            </div>

                            <span className='textoDifuminado' data-text={client.fullName} >
                                <b>{client.fullName}</b>
                            </span>
                        </button>

                    </Col>
                )))}

                <Col xs={12} sm={6} md={4} lg={3} className="listColStyle" >
                    <button className="listItemStyle add" onClick={redirectToCreateClient}>
                        <div className="icon-container">
                            <BsFillPlusCircleFill className='plusIconStyle' size={60} color='#e6af2e' alt="Add Icon" />
                        </div>
                        <span className='addText'>
                            <b><FormattedMessage id="project.users.addClient" /></b>
                        </span>
                    </button>
                </Col>

            </Row>

            <Errors errors={error} onClose={() => setError(null)} />

        </div>

    );

}

export default ClientsList;
