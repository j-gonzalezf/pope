import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import { GiFishing, GiSadCrab, GiWeight } from "react-icons/gi";
import { RiZzzFill } from "react-icons/ri";
import { TbFishBone } from "react-icons/tb";
import '../../users/components/ClientDetails.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';

const Graphs = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { clientId } = useParams();

    const [error, setError] = useState(null);

    const handleButtonClick = (sensationKey) => {
        navigate(`/tracking/graph/${sensationKey}`);
      };

    useEffect(() => {
        dispatch(actions.getSensations(clientId,
            () => { },
            errors => setError(errors)));
    }, [dispatch, clientId]);

    return (

        <div fluid="true" className='ClientDetails'>

            <h3 className="title">
                <FormattedMessage id="project.users.tracking" />
            </h3>

            <Row className="listStyle clientDetails">

                <Col xs={12} sm={6} md={4} lg={3} className="listColStyle">
                    <button className="listItemStyle clientDetails" onClick={() => handleButtonClick('fatigue')}>
                        <div className="align-content-center">
                            <TbFishBone className='plusIconStyle' size={60} color='#e6af2e' alt="Fish Bone Icon" />
                        </div>
                        <span className='clientDetailsText'>
                            <b><FormattedMessage id="project.tracking.fatigue" /></b>
                        </span>
                    </button>
                </Col>
                <Col xs={12} sm={6} md={4} lg={3} className="listColStyle">
                    <button className="listItemStyle clientDetails" onClick={() => handleButtonClick('stiffness')}>
                        <div>
                            <GiSadCrab className='plusIconStyle' size={60} color='#e6af2e' alt="Broken Crab Icon" />
                        </div>
                        <span className='clientDetailsText'>
                            <b><FormattedMessage id="project.tracking.stiffness" /></b>
                        </span>
                    </button>
                </Col>
                <Col xs={12} sm={6} md={4} lg={3} className="listColStyle">
                    <button className="listItemStyle clientDetails" onClick={() => handleButtonClick('motivation')}>
                        <div>
                            <GiFishing className='plusIconStyle' size={60} color='#e6af2e' alt="Motivation Icon" />
                        </div>
                        <span className='clientDetailsText'>
                            <b><FormattedMessage id="project.tracking.motivation" /></b>
                        </span>
                    </button>
                </Col>
                <Col xs={12} sm={6} md={4} lg={3} className="listColStyle" onClick={() => handleButtonClick('sleep')}>
                    <button className="listItemStyle clientDetails">
                        <div>
                            <RiZzzFill className='plusIconStyle' size={60} color='#e6af2e' alt="Zzz Icon" />
                        </div>
                        <span className='clientDetailsText'>
                            <b><FormattedMessage id="project.tracking.sleep" /></b>
                        </span>
                    </button>
                </Col>
                <Col xs={12} sm={6} md={4} lg={3} className="listColStyle">
                    <button className="listItemStyle clientDetails">
                        <div>
                            <GiWeight className='plusIconStyle' size={60} color='#e6af2e' alt="Weight Icon" />
                        </div>
                        <span className='clientDetailsText'>
                            <b><FormattedMessage id="project.users.weight" /></b>
                        </span>
                    </button>
                </Col>

            </Row>

            <Errors errors={error} onClose={() => setError(null)} />

        </div>

    );
}

export default Graphs;
