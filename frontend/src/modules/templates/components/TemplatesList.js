import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import { BsQuestionCircle } from "react-icons/bs";
import './TemplatesList.css';

import { useEffect, useState } from 'react';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';
import * as userSelectors from '../../users/selectors';
import AddTemplate from './AddTemplate';

const TemplatesList = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { clientId } = useParams();
    const { cycleId } = useParams();

    const role = useSelector(userSelectors.getUserRole);
    const getTemplates = useSelector(selectors.getTemplates);
    const [error, setError] = useState(null);

    const redirectToTemplateView = (templateId) => {
        navigate(`/templates/${clientId}/trainingCycle/${cycleId}/template/${templateId}`);
    };

    useEffect(() => {
        dispatch(actions.getTemplates(cycleId,
            () => { },
            errors => setError(errors)));

        dispatch(actions.getCycle(cycleId,
            () => { },
            errors => setError(errors)));
    }, [dispatch, cycleId]);

    const renderTooltip = (props) => (
        <Tooltip id="button-tooltip" {...props}>
            <FormattedMessage id="project.tooltips.templatesList" />
        </Tooltip>
    );

    return (

        <div fluid="true" className='TemplatesList'>

            <h3 className="title">
                <FormattedMessage id="project.templates.templatesList.title" />
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

            {getTemplates && getTemplates.length > 0 ? (
                <Row className="listStyle">
                    {getTemplates.map((template) => (
                        <Col xs={12} sm={6} md={4} lg={3} className="listColStyle template" key={template.id} >
                            <button className="listItemStyle button" onClick={() => redirectToTemplateView(template.id)}>
                                <div className="template-item">
                                    <p className='text-item'>{template.name}</p>
                                </div>
                            </button>
                        </Col>
                    ))}
                    {role === 'TRAINER' && (
                        <Col xs={12} sm={12} md={12} lg={12} className="listItemStyle template add">
                            <AddTemplate
                                error={error}
                                setError={setError}
                            />
                        </Col>
                    )}
                </Row>
            ) : (
                <div className="empty-template">
                    <p className='text-white'>
                        <FormattedMessage id="project.templates.templatesList.empty" />
                    </p>

                    {role === 'TRAINER' && (
                        <AddTemplate
                            error={error}
                            setError={setError}
                        />
                    )}
                </div>
            )}

            <Errors errors={error} onClose={() => setError(null)} />

        </div>

    );

}

export default TemplatesList;
