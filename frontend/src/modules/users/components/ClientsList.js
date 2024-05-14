import Card from 'react-bootstrap/Card';
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

    useEffect(() => {

        dispatch(actions.getClients(user.id,
            () => { },
            errors => setError(errors)));

    }, [dispatch, user.id]);

    return (

        <div fluid className='ClientsList'>

                <Row className="listStyle">
                    {getClients && (getClients.map((client) => (
                        <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle" key={client.id}>
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
                            <h5>{client.fullName}</h5>
                        </Col>
                    )))}
                    <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle">
                        <Card className='text-center' onClick={redirectToCreateClient}>
                            <BsFillPlusCircleFill className='plus' size={50} color='grey' />
                            <Card.Body>
                                <Card.Title className='text-center'>
                                    <b><FormattedMessage id="project.users.addClient" /></b>
                                </Card.Title>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>

            <Errors errors={error} onClose={() => setError(null)} />
        </div>

    );

}

export default ClientsList;
