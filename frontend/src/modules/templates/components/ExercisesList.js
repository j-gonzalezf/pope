import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import { BsArrowUpCircleFill, BsFillPlusCircleFill } from "react-icons/bs";
import './ExercisesList.css';

import { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as userSelectors from '../../users/selectors';

const ExercisesList = () => {

    const dispatch = useDispatch();

    const user = useSelector(userSelectors.getUser);

    const [name, setName] = useState('');
    const [description, setDescription] = useState(null);
    const [type, setType] = useState(null);
    const [bodyPart, setBodyPart] = useState(null);
    const [equipment, setEquipment] = useState(null);
    const [link, setLink] = useState(null);
    const [error, setError] = useState(null);
    const [showAddExerciseModal, setShowAddExerciseModal] = useState(false);

    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.addExercise(
                {
                    name: name.trim(),
                    description: description,
                    type: type,
                    bodyPart: bodyPart,
                    equipment: equipment,
                    link: link,
                    trainerId: user.id
                },
                () => {
                    setShowAddExerciseModal(false);
                    setName('');
                    setDescription(null);
                    setType(null);
                    setBodyPart(null);
                    setEquipment(null);
                    setLink(null);
                    setError(null);
                },
                errors => setError(errors)
            ));

        } else {
            setError(null);
            form.classList.add('was-validated');
        }
    }

    const scrollToTop = () => {
        window.scrollTo(0, 0);
    }

    return (

        <div fluid className='ExercisesList'>

            <h3 className="title">
                <FormattedMessage id="project.templates.exercisesList.title" />
            </h3>

            <Modal show={showAddExerciseModal} onHide={() => setShowAddExerciseModal(false)} >

                <Modal.Header className="modal-header">
                    <Modal.Title>
                        <FormattedMessage id="project.templates.addExercise.title" />
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body className="modal-body exercise">

                    <Form
                        ref={node => form = node}
                        noValidate
                        onSubmit={handleSubmit}
                    >
                        <Form.Group className="mb-3" controlId="name">
                            <Form.Label>
                                <FormattedMessage id="project.templates.exerciseName" />
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Introduzca el nombre del ejercicio"
                                value={name}
                                onChange={e => setName(e.target.value)}
                                required
                                autoFocus
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.exerciseNameRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="description">
                            <Form.Label>
                                <FormattedMessage id="project.templates.exerciseDescription" />
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                as="textarea" rows={3}
                                placeholder="Introduzca una descripción para el ejercicio"
                                value={description}
                                onChange={e => setDescription(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.exerciseDescriptionRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="type">
                            <Form.Label>
                                <FormattedMessage id="project.templates.exerciseType" />
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Introduzca la categoría de ejercicio"
                                value={type}
                                onChange={e => setType(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.exerciseTypeRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="bodyPart">
                            <Form.Label>
                                <FormattedMessage id="project.templates.exerciseBodyPart" />
                            </Form.Label>
                            <Form.Control
                                type="text"
                                title="Introduzca la parte del cuerpo en la que se enfoca el ejercicio"
                                placeholder="Introduzca la parte del cuerpo en la que se enfoca el e..."
                                value={bodyPart}
                                onChange={e => setBodyPart(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="equipment">
                            <Form.Label>
                                <FormattedMessage id="project.templates.exerciseEquipment" />
                            </Form.Label>
                            <Form.Control
                                type="text"
                                title="Introduzca el equipamiento necesario para realizar el ejercicio"
                                placeholder="Introduzca el equipamiento necesario para realizar el e..."
                                value={equipment}
                                onChange={e => setEquipment(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="link">
                            <Form.Label>
                                <FormattedMessage id="project.templates.exerciseLink" />
                            </Form.Label>
                            <Form.Control
                                type="url"
                                title="Introduzca un enlace de referencia para la correcta realización del ejercicio"
                                placeholder="Introduzca un enlace de referencia para la correcta rea..."
                                value={link}
                                onChange={e => setLink(e.target.value)}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.exerciseLinkPattern" />
                            </Form.Control.Feedback>
                        </Form.Group>

                    </Form>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Modal.Body>

                <Modal.Footer className="modal-footer">
                    <Button variant="secondary" onClick={() => setShowAddExerciseModal(false)}>
                        <FormattedMessage id="project.global.button.cancel" />
                    </Button>
                    <Button className="primary cycle" onClick={handleSubmit}>
                        <FormattedMessage id="project.templates.addExercise.button" />
                    </Button>
                </Modal.Footer>

            </Modal>

                    
            <Button className="primary exercise" onClick={() => setShowAddExerciseModal(true)} >
                <BsFillPlusCircleFill className="plusIconStyle exercise" />
                <span>
                    <b><FormattedMessage id="project.templates.addExercise" /></b>
                </span>
            </Button>
            <br />

            <BsArrowUpCircleFill className="arrowIconStyle" size={30} onClick={scrollToTop} title="Volver arriba" />

        </div>

    );

}

export default ExercisesList;
