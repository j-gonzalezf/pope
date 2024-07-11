import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Table from 'react-bootstrap/Table';
import { BsArrowUpCircleFill, BsFillPlusCircleFill, BsPencilSquare, BsTrash } from "react-icons/bs";
import './ExercisesList.css';

import { useState, useEffect } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as userSelectors from '../../users/selectors';
import * as selectors from '../selectors';

const ExercisesList = () => {

    const dispatch = useDispatch();

    const user = useSelector(userSelectors.getUser);
    const getExercises = useSelector(selectors.getExercises);

    const [id, setId] = useState(null);
    const [name, setName] = useState('');
    const [description, setDescription] = useState(null);
    const [type, setType] = useState(null);
    const [bodyPart, setBodyPart] = useState(null);
    const [equipment, setEquipment] = useState(null);
    const [link, setLink] = useState(null);
    const [error, setError] = useState(null);
    const [showAddExerciseModal, setShowAddExerciseModal] = useState(false);
    const [showUpdateExerciseModal, setShowUpdateExerciseModal] = useState(false);

    let formCreate;
    let formUpdate;

    const handleSubmit = event => {

        event.preventDefault();

        if (formCreate.checkValidity()) {

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
                    closeModal();
                    dispatch(actions.clearExercise());
                    dispatch(actions.getExercises(user.id,
                        () => { },
                        errors => setError(errors)));
                },
                errors => setError(errors)
            ));

        } else {
            setError(null);
            formCreate.classList.add('was-validated');
        }
    }

    const handleUpdateExercise = event => {

        event.preventDefault();

        if (formUpdate.checkValidity()) {

            dispatch(actions.updateExercise(
                {
                    id: id,
                    name: name.trim(),
                    description: description,
                    type: type,
                    bodyPart: bodyPart,
                    equipment: equipment,
                    link: link,
                    trainerId: user.id
                },
                () => {
                    closeModal();
                    dispatch(actions.clearExercise());
                    dispatch(actions.getExercises(user.id,
                        () => { },
                        errors => setError(errors)));
                },
                errors => setError(errors)
            ));

        } else {
            setError(null);
            formUpdate.classList.add('was-validated');
        }

    }

    const deleteExercise = exercise => {
        dispatch(actions.deleteExercise(exercise.id,
            () => {
                dispatch(actions.getExercises(user.id,
                    () => { },
                    errors => setError(errors)));
            },
            errors => setError(errors)
        ));
    }

    const openUpdateExerciseModal = exercise => {
        setId(exercise.id);
        setName(exercise.name);
        setDescription(exercise.description);
        setType(exercise.type);
        setBodyPart(exercise.bodyPart);
        setEquipment(exercise.equipment);
        setLink(exercise.link);
        setShowUpdateExerciseModal(true);
    }

    const closeModal = () => {
        setShowAddExerciseModal(false);
        setShowUpdateExerciseModal(false);
        setId(null);
        setName('');
        setDescription(null);
        setType(null);
        setBodyPart(null);
        setEquipment(null);
        setLink(null);
        setError(null);
    }

    const scrollToTop = () => {
        window.scrollTo(0, 0);
    }

    useEffect(() => {
        dispatch(actions.getExercises(user.id,
            () => { },
            errors => setError(errors)));
    }, [dispatch, user.id]);

    return (

        <div fluid className='ExercisesList'>

            <h3 className="title">
                <FormattedMessage id="project.templates.exercisesList.title" />
            </h3>

            <Modal show={showAddExerciseModal} onHide={closeModal} >

                <Modal.Header className="modal-header">
                    <Modal.Title>
                        <FormattedMessage id="project.templates.addExercise.title" />
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body className="modal-body exercise">

                    <Form
                        ref={node => formCreate = node}
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

                        <Form.Group className="mb-3 required">
                            <Form.Label data-testid="requiredFields" htmlFor="requiredFields" className='required text'>
                                <FormattedMessage id="project.common.requiredFields" />
                                <span className='required'>*</span>
                            </Form.Label>
                        </Form.Group>

                    </Form>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Modal.Body>

                <Modal.Footer className="modal-footer">
                    <Button variant="secondary" onClick={closeModal}>
                        <FormattedMessage id="project.global.button.cancel" />
                    </Button>
                    <Button className="primary cycle" onClick={handleSubmit}>
                        <FormattedMessage id="project.templates.addExercise.button" />
                    </Button>
                </Modal.Footer>

            </Modal>

            <Modal show={showUpdateExerciseModal} onHide={closeModal} className='modal'>

                <Modal.Header className='modal-header'>
                    <Modal.Title as="h3">
                        <FormattedMessage id="project.templates.updateExercise.title" />
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body className='modal-body cycle'>
                    <Form
                        ref={node => formUpdate = node}
                        className="needs-validation"
                        noValidate
                        onSubmit={e => handleUpdateExercise(e)}
                    >
                        <Form.Group className="mb-3" controlId="name">
                            <Form.Label>
                                <FormattedMessage id="project.templates.exerciseName" />
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

                <Modal.Footer className='modal-footer'>
                    <Button variant="secondary" onClick={closeModal}>
                        <FormattedMessage id="project.global.button.cancel" />
                    </Button>
                    <Button className="primary cycle" onClick={handleUpdateExercise}>
                        <FormattedMessage id="project.templates.updateCycle" />
                    </Button>
                </Modal.Footer>

            </Modal>

            <div class="table-responsive">
                <Table className="table">
                    <thead>
                        <tr>
                            <th className="customTable"></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.exerciseName" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.exerciseDescription" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.exerciseType" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.exerciseBodyPart" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.exerciseEquipment" /></th>
                            <th className="customTable"></th>
                        </tr>
                    </thead>
                    <tbody>
                        {getExercises.map((exercise) => (
                            <tr key={exercise.id} >
                                <td className="customTable"></td>
                                <td className="customTable">
                                    {exercise.link ? (
                                        /* Target hace que el enlace aparezca en una nueva pestaña
                                        *  y rel se usa como un atributo de seguridad 
                                        */
                                        <a className="link" href={exercise.link} target="_blank" rel="noopener noreferrer">
                                            {exercise.name}
                                        </a>
                                    ) : (
                                        exercise.name
                                    )}
                                </td>
                                <td className="customTable">{exercise.description}</td>
                                <td className="customTable">{exercise.type}</td>
                                <td className="customTable">{exercise.bodyPart}</td>
                                <td className="customTable">{exercise.equipment}</td>
                                <td className="customTable edit">
                                    <Button className="primary edit" onClick={(event) => {
                                        event.stopPropagation(); // Para que entre en updateCycle y no en cycleDetails
                                        openUpdateExerciseModal(exercise);
                                    }}
                                        title='Pulsa para editar ejeercicio'>
                                        <BsPencilSquare className="editIconStyle cycle" />
                                        <span>
                                            <FormattedMessage id="project.templates.updateCycle" />
                                        </span>
                                    </Button>
                                </td>
                                <td className="customTable delete">
                                    <Button className="primary delete" onClick={(event) => {
                                        event.stopPropagation(); // Para que entre en updateCycle y no en cycleDetails
                                        deleteExercise(exercise);
                                    }}
                                        title='Pulsa para eliminar ejercicio'>
                                        <BsTrash className="deleteIconStyle cycle" />
                                        <span>
                                            <FormattedMessage id="project.templates.deleteCycle" />
                                        </span>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        <tr>
                            <td className="customTable"></td>
                            <td colSpan={6} className="customTable">
                                <Button className="primary exercise" onClick={() => setShowAddExerciseModal(true)} >
                                    <BsFillPlusCircleFill className="plusIconStyle exercise" />
                                    <span>
                                        <b><FormattedMessage id="project.templates.addExercise" /></b>
                                    </span>
                                </Button>
                            </td>
                        </tr>
                    </tbody>
                </Table>

                <BsArrowUpCircleFill className="arrowIconStyle" size={30} onClick={scrollToTop} title="Volver arriba" />

            </div>

            <Errors errors={error} onClose={() => setError(null)} />

        </div>

    );

}

export default ExercisesList;
