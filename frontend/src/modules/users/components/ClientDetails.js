import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import { GiCannedFish, GiFishingNet } from "react-icons/gi";
import { LiaDharmachakraSolid } from "react-icons/lia";
import './ClientDetails.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../../users/actions';

const ClientDetails = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { clientId } = useParams();

    const [error, setError] = useState(null);

    useEffect(() => {
        dispatch(actions.getClientInfo(clientId,
            () => { },
            errors => setError(errors)));
    }, [dispatch, clientId]);

    const redirectToCyclesList = (clientId) => {
        navigate('/templates/trainingCycles/' + clientId);
    }

    const redirectToUpdateClient = (clientId) => {
        navigate('/users/updateClient/' + clientId);
    }

    return (

        <div fluid className='ClientDetails'>

            <Errors errors={error} onClose={() => setError(null)} />

            <Row className="listStyle clientDetails">

                <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle clientDetails" onClick={() => redirectToCyclesList(clientId)}>
                    <div className="align-content-center">
                        <GiFishingNet className='plusIconStyle' size={60} color='#e6af2e' alt="Fishing Net Icon" />
                    </div>
                    <span className='clientDetailsText'>
                        <b><FormattedMessage id="project.users.templates" /></b>
                    </span>
                </Col>
                <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle clientDetails">
                    <div>
                        <LiaDharmachakraSolid className='plusIconStyle' size={60} color='#e6af2e' alt="Dharmachakra Icon" />
                    </div>
                    <span className='clientDetailsText'>
                        <b><FormattedMessage id="project.users.tracking" /></b>
                    </span>
                </Col>
                <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle clientDetails" onClick={() => redirectToUpdateClient(clientId)}>
                    <div>
                        <GiCannedFish className='plusIconStyle' size={60} color='#e6af2e' alt="Canned Fish Icon" />
                    </div>
                    <span className='clientDetailsText'>
                        <b><FormattedMessage id="project.users.clientDetails" /></b>
                    </span>
                </Col>

            </Row>

        </div>

    );

}

export default ClientDetails;
