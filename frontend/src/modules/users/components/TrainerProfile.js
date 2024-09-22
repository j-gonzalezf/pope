import Col from 'react-bootstrap/Col';
import Image from 'react-bootstrap/Image';
import Row from 'react-bootstrap/Row';
import './ClientsList.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';
import TrainerIcon from '../../common/images/trainer-logo.webp';

const TrainerProfile = () => {

    const dispatch = useDispatch();

    const trainer = useSelector(selectors.getTrainerInfo);
    const { trainerId } = useParams();

    const [error, setError] = useState(null);

    useEffect(() => {
        dispatch(actions.getTrainerInfo(trainerId,
            () => { },
            errors => setError(errors)));
    }, [dispatch, trainerId]);

    return (
        <div fluid="true" className="ClientsList">

            <h3 className="title">
                {trainer?.fullName}
            </h3>
            <br />
            <br />
            <br />
            <Row className='bigListStyle'>
                <Col xs={12} md={6} className="bigImageStyle">
                    {trainer?.icon !== undefined ?
                        <div className="bigImageStyle">
                            <Image roundedCircle fluid src={"data:image/png;base64," + trainer.icon.base64} alt={trainer.icon.name} />
                        </div>
                        : (
                            <div className="bigImageStyle">
                                <Image roundedCircle fluid src={TrainerIcon} alt="Trainer icon" className="bigImageStyle" />
                            </div>
                        )}
                </Col>
                <Col xs={12} md={6} className="text-left">
                    <h5 className="title text-left">
                        <FormattedMessage id="project.users.email" />:
                    </h5>
                    <p className="title text-left">{trainer?.email}</p>
                    <br />
                    <h5 className="title text-left">
                        <FormattedMessage id="project.users.phoneContact" />:
                    </h5>
                    <p className="title text-left">{trainer?.phone}</p>
                    <br />
                    <h5 className="title text-left">
                        <FormattedMessage id="project.users.socialLinks" />:
                    </h5>
                    <Link to={`${trainer?.socialLinks}`} className='title link text-left' target="_blank" rel="noopener noreferrer">
                        {trainer?.socialLinks}
                    </Link>
                </Col>
            </Row>

            <Errors errors={error} onClose={() => setError(null)} />

        </div>
    );

}

export default TrainerProfile;
