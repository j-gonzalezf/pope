import './TemplatesList.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import AddTemplate from './AddTemplate';

const TemplatesList = () => {

    const dispatch = useDispatch();

    const { cycleId } = useParams();

    const [templates, setTemplates] = useState([]);
    const [name, setName] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        dispatch(actions.getCycle(cycleId,
            () => { },
            errors => setError(errors)));
    }, [dispatch, cycleId]);

    return (
        <div fluid className='TemplatesList'>

            <h3 className="title">
                <FormattedMessage id="project.templates.templatesList.title" />
            </h3>

            {templates.length === 0 ? (
                <div className="empty-template">
                    <AddTemplate
                        name={name}
                        setName={setName}
                        error={error}
                        setError={setError}
                    />
                </div>
            ) : (
                <div className="template-list">
                    {templates.map((template, index) => (
                        <div key={index} className="template-item">
                            <p className='text-white'>{template.name}</p>
                        </div>
                    ))}

                    <AddTemplate
                        name={name}
                        setName={setName}
                        error={error}
                        setError={setError}
                    />
                </div>
            )}

            <Errors errors={error} onClose={() => setError(null)} />

        </div>

    );

}

export default TemplatesList;
