import Col from 'react-bootstrap/Col';
import Image from 'react-bootstrap/Image';
import Row from 'react-bootstrap/Row';
import { BsFillPlusCircleFill, BsQuestionCircle } from "react-icons/bs";
import './ClientsList.css';

import { useCallback, useEffect, useState } from 'react';
import { Tooltip, OverlayTrigger } from 'react-bootstrap';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Errors } from '../../common';
import DefaultIcon from '../../common/images/client-logo.webp';
import * as actions from '../actions';
import * as trackingActions from '../../tracking/actions';
import * as selectors from '../selectors';

const ClientsList = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [error, setError] = useState(null);

    const user = useSelector(selectors.getUser);
    const getClients = useSelector(selectors.getClients);

    const redirectToCreateClient = useCallback(() => {
        navigate('/users/addClient');
    }, [navigate]);

    const redirectToClientDetails = useCallback((clientId) => {
        navigate(`/users/clientDetails/${clientId}`);
    }, [navigate]);


    useEffect(() => {

        dispatch(trackingActions.clearSensations());

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

    const renderTooltip = (props) => (
        <Tooltip id="button-tooltip" {...props}>
            <FormattedMessage id="project.tooltips.clientsList.p1" />
            <br />
            <FormattedMessage id="project.tooltips.clientsList.p2" />
        </Tooltip>
    );

    return (

        <div fluid="true" className='ClientsList'>

            <h3 className="title">
                <FormattedMessage id="project.users.clients.title" />
                <OverlayTrigger
                    placement="right"
                    delay={{ show: 200, hide: 400 }}
                    overlay={renderTooltip}
                >
                    <span className="d-inline-block" style={{ marginLeft: '10px' }}>
                        <BsQuestionCircle className="checkIconStyle" color='#e6af2e' size={20} />
                    </span>
                </OverlayTrigger>
            </h3>

            <Row className="listStyle">

                {getClients && (getClients.map((client) => (

                    <Col xs={12} sm={6} md={4} lg={3} key={client.id} className="listColStyle" >

                        <button className="listItemStyle" onClick={() => redirectToClientDetails(client.id)}>
                            <div className="image-container">
                                {client.icon ? (
                                    <Image
                                        src={`/user-icons/${client.icon}`}
                                        alt="Client Icon"
                                        className="smallImageStyle"
                                    />
                                ) : (
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
