import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Table from 'react-bootstrap/Table';
import { BsChevronRight, BsFillPlusCircleFill, BsPencilSquare, BsQuestionCircle, BsTrash } from "react-icons/bs";
import './CyclesList.css';

import { useEffect, useState } from 'react';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';
import * as userSelectors from '../../users/selectors';

const CyclesList = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const user = useSelector(userSelectors.getUser);
    const role = useSelector(userSelectors.getUserRole);
    const { clientId } = useParams();
    const getCycles = useSelector(selectors.getCycles);

    const [id, setId] = useState(null);
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [error, setError] = useState(null);
    const [showAddCycleModal, setShowAddCycleModal] = useState(false);
    const [showUpdateCycleModal, setShowUpdateCycleModal] = useState(false);

    let formCreate;
    let formUpdate;

    function dateFormat(date) {
        let d = new Date(date);
        let month = '' + (d.getMonth() + 1);
        let day = '' + d.getDate();
        let year = d.getFullYear().toString().slice(-2);

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;
        return [day, month, year].join('/');
    }

    const handleSubmit = event => {

        event.preventDefault();

        if (formCreate.checkValidity()) {

            dispatch(actions.createCycle(
                {
                    name: name.trim(),
                    description: description ? description.trim() : null,
                    fromDate: fromDate || null,
                    toDate: toDate || null,
                    trainerId: user.id,
                    clientId: clientId
                },
                () => {
                    closeModal();
                    dispatch(actions.clearCycle());
                    dispatch(actions.getCycles(user.id, clientId,
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

    const handleUpdateCycle = event => {

        event.preventDefault();

        if (formUpdate.checkValidity()) {

            dispatch(actions.updateCycle(
                {
                    id: id,
                    name: name.trim(),
                    description: description ? description.trim() : null,
                    fromDate: fromDate || null,
                    toDate: toDate || null,
                    trainerId: user.id,
                    clientId: clientId
                },
                () => {
                    closeModal();
                    dispatch(actions.clearCycle());
                    dispatch(actions.getCycles(user.id, clientId,
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

    const deleteCycle = cycle => {
        dispatch(actions.deleteCycle(cycle.id,
            () => {
                dispatch(actions.getCycles(user.id, clientId,
                    () => { },
                    errors => setError(errors)));
            },
            errors => setError(errors)
        ));
    }

    const openUpdateCycleModal = cycle => {
        setId(cycle.id);
        setName(cycle.name);
        setDescription(cycle.description);
        setFromDate(cycle.fromDate);
        setToDate(cycle.toDate);
        setShowUpdateCycleModal(true);
    }

    const closeModal = () => {
        setShowAddCycleModal(false);
        setShowUpdateCycleModal(false);
        setId(null);
        setName('');
        setDescription('');
        setFromDate('');
        setToDate('');
    }

    const redirectToCycleDetails = cycle => {
        navigate(`/templates/${clientId}/trainingCycle/${cycle.id}`);
    }

    useEffect(() => {
        if(role === "CLIENT") {
            dispatch(actions.getCycles(user.trainerId, clientId,
                () => { },
                errors => setError(errors)));
        } else {
            dispatch(actions.getCycles(user.id, clientId,
                () => { },
                errors => setError(errors)));
        }
    }, [dispatch, role, user.trainerId, user.id, clientId]);

    const renderTooltip = (props) => (
        <Tooltip id="button-tooltip" {...props}>
            <FormattedMessage id="project.tooltips.cyclesList" />
        </Tooltip>
    );

    return (

        <div fluid="true" className='CyclesList'>

            <h3 className="title">
                <FormattedMessage id="project.templates.cycles.title" />
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

            <Modal show={showAddCycleModal} onHide={closeModal} className='modal'>

                <Modal.Header className='modal-header'>
                    <Modal.Title as="h3">
                        <FormattedMessage id="project.templates.addCycle.title" />
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body className='modal-body cycle'>
                    <Form
                        ref={node => formCreate = node}
                        className="needs-validation"
                        noValidate
                        onSubmit={e => handleSubmit(e)}
                    >
                        <Form.Group className="mb-3">

                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleName" />
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Introduzca el nombre del ciclo de entrenamiento"
                                value={name}
                                onChange={e => setName(e.target.value)}
                                required
                                autoFocus
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleNameRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleDescription" />
                            </Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                placeholder="Introduzca una descripción para el ciclo de entrenamiento"
                                value={description}
                                onChange={e => setDescription(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleFromDate" />
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={fromDate}
                                onChange={e => setFromDate(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleFromDateRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleToDate" />
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={toDate}
                                onChange={e => setToDate(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleToDateRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3 required">
                            <Form.Label data-testid="requiredFields" className='required text'>
                                <FormattedMessage id="project.common.requiredFields" />
                                <span className='required'>*</span>
                            </Form.Label>
                        </Form.Group>

                    </Form>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Modal.Body>

                <Modal.Footer className='modal-footer'>
                    <Button variant="secondary" onClick={closeModal}>
                        <FormattedMessage id="project.global.button.cancel" />
                    </Button>
                    <Button className="primary cycle" onClick={handleSubmit}>
                        <FormattedMessage id="project.templates.addCycle.button" />
                    </Button>
                </Modal.Footer>

            </Modal>

            <Modal show={showUpdateCycleModal} onHide={closeModal} className='modal'>

                <Modal.Header className='modal-header'>
                    <Modal.Title as="h3">
                        <FormattedMessage id="project.templates.updateCycle.title" />
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body className='modal-body cycle'>
                    <Form
                        ref={node => formUpdate = node}
                        className="needs-validation"
                        noValidate
                        onSubmit={e => handleUpdateCycle(e)}
                    >
                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleName" />
                            </Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Introduzca el nombre del ciclo de entrenamiento"
                                value={name}
                                onChange={e => setName(e.target.value)}
                                required
                                autoFocus
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleNameRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleDescription" />
                            </Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                placeholder="Introduzca una descripción para el ciclo de entrenamiento"
                                value={description || ''}
                                onChange={e => setDescription(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleFromDate" />
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={fromDate}
                                onChange={e => setFromDate(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleFromDateRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleToDate" />
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={toDate}
                                onChange={e => setToDate(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleToDateRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                    </Form>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Modal.Body>

                <Modal.Footer className='modal-footer'>
                    <Button variant="secondary" onClick={closeModal}>
                        <FormattedMessage id="project.global.button.cancel" />
                    </Button>
                    <Button className="primary cycle" onClick={handleUpdateCycle}>
                        <FormattedMessage id="project.templates.updateCycle" />
                    </Button>
                </Modal.Footer>

            </Modal>

            <div className="table-responsive">
                <Table className="table">
                    <thead>
                        <tr>
                            <th className="customTable"></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.cycleName" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.cycleDescription" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.cycleFrom" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.cycleTo" /></th>
                            <th className="customTable"></th>
                        </tr>
                    </thead>
                    <tbody>
                        {getCycles.map((cycle) => (
                            <tr key={cycle.id} className="cycleSelect" tabIndex="0"
                                onClick={() => redirectToCycleDetails(cycle)}
                                onKeyDown={(event) => {
                                    if (event.key === 'Enter') {
                                        redirectToCycleDetails(cycle);
                                    }
                                }}>
                                <td className="pointer"><BsChevronRight size={15} /></td>
                                <td className="customTable cycleName">{cycle.name}</td>
                                <td className="customTable">{cycle.description}</td>
                                <td className="customTable">{dateFormat(cycle.fromDate)}</td>
                                <td className="customTable">{dateFormat(cycle.toDate)}</td>
                                {role === 'TRAINER' && (
                                    <>
                                        <td className="customTable edit">
                                            <Button className="primary edit" onClick={(event) => {
                                                event.stopPropagation(); // Para que entre en updateCycle y no en cycleDetails
                                                openUpdateCycleModal(cycle);
                                            }}
                                                title='Pulsa para editar ciclo'>
                                                <BsPencilSquare className="editIconStyle cycle" />
                                                <span>
                                                    <FormattedMessage id="project.templates.updateCycle" />
                                                </span>
                                            </Button>
                                        </td>
                                        <td className="customTable delete">
                                            <Button className="primary delete" onClick={(event) => {
                                                event.stopPropagation(); // Para que entre en updateCycle y no en cycleDetails
                                                deleteCycle(cycle);
                                            }}
                                                title='Pulsa para eliminar ciclo'>
                                                <BsTrash className="deleteIconStyle cycle" />
                                                <span>
                                                    <FormattedMessage id="project.templates.deleteCycle" />
                                                </span>
                                            </Button>
                                        </td>
                                    </>
                                )}
                            </tr>
                        ))}
                        {role === 'TRAINER' && (
                            <tr>
                                <td className="customTable"></td>
                                <td colSpan={4} className="customTable">
                                    <Button className="primary cycle" onClick={() => setShowAddCycleModal(true)}>
                                        <BsFillPlusCircleFill className="plusIconStyle cycle" />
                                        <span>
                                            <b><FormattedMessage id="project.templates.addCycle" /></b>
                                        </span>
                                    </Button>
                                </td>
                            </tr>
                        )}
                    </tbody>
                </Table>
            </div>

            <Errors errors={error} onClose={() => setError(null)} />

        </div>

    );

}

export default CyclesList;
