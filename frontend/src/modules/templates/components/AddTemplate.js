import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { BsCheckSquareFill, BsFillPlusCircleFill, BsXSquareFill } from "react-icons/bs";
import './TemplatesList.css';

import { useEffect, useRef, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';

const AddTemplate = ({ error, setError }) => {

    const dispatch = useDispatch();
    const { cycleId } = useParams();
    const wrapperRef = useRef(null);

    const [name, setName] = useState('');
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
                    dispatch(actions.getTemplates(cycleId,
                        () => { },
                        errors => setError(errors)));
                },
                errors => setError(errors)));
        }
    }

    /* Función que detecta clicks fuera del componente para cerrarlo */
    useEffect(() => {
        function handleClickOutside(event) {
            if (wrapperRef.current && !wrapperRef.current.contains(event.target)) {
                setShowInput(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [wrapperRef]);

    return (

        showInput ? (
            <div ref={wrapperRef} className="addTemplateRow" >
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
                            <Button className="primary template" onClick={() => { setShowInput(false); setName('') }}>
                                <BsXSquareFill className="crossIconStyle" color='gray' size={20} />
                            </Button>
                        </div>
                    </Form.Group>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Form>
            </div>
        ) : (
            <Button className="primary" onClick={() => setShowInput(true)} >
                <BsFillPlusCircleFill className="plusIconStyle cycle" />
                <span>
                    <b><FormattedMessage id="project.templates.addTemplate" /></b>
                </span>
            </Button>
        )
    );

}

export default AddTemplate;
