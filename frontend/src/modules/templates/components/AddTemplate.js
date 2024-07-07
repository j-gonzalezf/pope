import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { BsCheckSquareFill, BsFillPlusCircleFill, BsXSquareFill } from "react-icons/bs";
import './TemplatesList.css';

import { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';

const AddTemplate = ({ name, setName, error, setError }) => {

    const dispatch = useDispatch();
    const { cycleId } = useParams();
    
    const [showInput, setShowInput] = useState(false);

    let form;

    const handleSubmit = event => {
        event.preventDefault();
        if (form.checkValidity()) {
            dispatch(actions.createTemplate(
                {
                    name: name.trim(),
                    creationDate: new Date().toISOString().slice(0, -1),
                    cycleId: cycleId
                },
                () => {
                    setShowInput(false);
                    setName('');
                },
                errors => setError(errors)));
        }
    }

    return (

        showInput ? (
            <Form
                ref={node => form = node}
                className="needs-validation"
                noValidate
                onSubmit={e => handleSubmit(e)}
            >
                <Form.Group className="mb-3 template">
                    <Form.Control
                        type="text"
                        className="form-control"
                        id="name"
                        name="name"
                        placeholder="Nombre de la plantilla"
                        value={name}
                        onChange={e => setName(e.target.value)}
                        required
                        autoFocus
                    />
                    <div className="form-buttons">
                        <Button className="primary template" type="submit">
                            <BsCheckSquareFill className="checkIconStyle" color='#e6af2e' size={20} />
                        </Button>
                        <Button className="primary template" onClick={() => setShowInput(false)}>
                            <BsXSquareFill className="crossIconStyle" color='gray' size={20} />
                        </Button>
                    </div>
                </Form.Group>

                <Errors errors={error} onClose={() => setError(null)} />

            </Form>
        ) : (
            <div>
                <p className='text-white'>
                    <FormattedMessage id="project.templates.templatesList.empty" />
                </p>

                <Button className="primary cycle" onClick={() => setShowInput(true)} >
                    <BsFillPlusCircleFill className="plusIconStyle cycle" />
                    <span>
                        <b><FormattedMessage id="project.templates.addTemplate" /></b>
                    </span>
                </Button>
            </div>
        )
    );

}

export default AddTemplate;
