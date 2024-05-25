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

    const redirectToUpdateClient = (clientId) => {
        navigate('/users/updateClient/' + clientId);
    }

    return (

        <div fluid className='ClientDetails'>

            <Errors errors={error} onClose={() => setError(null)} />

            <Row className="listStyle">

                <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle">
                    <div className="align-content-center">
                        <GiFishingNet size={60} />
                    </div>
                    <div>
                        <br />
                        <h5><FormattedMessage id="project.templates.templates" /></h5>
                    </div>
                </Col>
                <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle">
                    <div>
                        <LiaDharmachakraSolid size={60} />
                    </div>
                    <div>
                        <br />
                        <h5><FormattedMessage id="project.templates.tracking" /></h5>
                    </div>
                </Col>
                <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle" onClick={() => redirectToUpdateClient(clientId)}>
                    <div>
                        <GiCannedFish size={60} />
                    </div>
                    <div>
                        <br />
                        <h5><FormattedMessage id="project.templates.clientDetails" /></h5>
                    </div>
                </Col>

            </Row>

        </div>

    );

}

export default ClientDetails;
