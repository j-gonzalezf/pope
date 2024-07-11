import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Table from 'react-bootstrap/Table';
import { BsFillPlusCircleFill } from "react-icons/bs";
import './TemplateView.css';

import { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';

import { Errors } from '../../common';
import AddTemplateRow from './AddTemplateRow';

const TemplateView = () => {

    const dispatch = useDispatch();

    const { templateId } = useParams();

    const [error, setError] = useState(null);
    const [showAddInput, setShowAddInput] = useState(false);

    return (

        <div fluid className='TemplateView'>

            <Row>

                <Col xs={12} sm={12} md={9} className='template'>
                    <h3 className="title templates">
                        <FormattedMessage id="project.templates.templateView.title" />
                    </h3>

                    <div class="table-responsive">
                        <Table className="table">
                            <thead>
                                <tr>
                                    <th colSpan={4} className="customTable title-template"><h4>Día de Pull</h4></th>
                                </tr>
                                <tr>
                                    <th className="customTable underline"><FormattedMessage id="project.templates.templateView.exercise" /></th>
                                    <th className="customTable underline"><FormattedMessage id="project.templates.templateView.series" /></th>
                                    <th className="customTable underline"><FormattedMessage id="project.templates.templateView.reps" /></th>
                                    <th className="customTable underline"><FormattedMessage id="project.templates.templateView.weight" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                {/*
                        {getCycles.map((cycle) => (
                            <tr key={cycle.id} className="cycleSelect" onClick={() => redirectToCycleDetails(cycle)}>
                                <td className="pointer"><BsChevronRight size={15} /></td>
                                <td className="customTable cycleName">{cycle.name}</td>
                                <td className="customTable">{cycle.description}</td>
                                <td className="customTable">{dateFormat(cycle.fromDate)}</td>
                                <td className="customTable">{dateFormat(cycle.toDate)}</td>
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
                            </tr>
                        ))}
                            */}
                                <tr>
                                    {showAddInput ? (
                                        <td colSpan={4} className="customTable">
                                            <AddTemplateRow />
                                        </td>
                                    ) : (
                                        <td colSpan={4} className="customTable">
                                            <Button className="primary cycle" onClick={() => setShowAddInput(true)}>
                                                <BsFillPlusCircleFill className="plusIconStyle cycle" />
                                                <span>
                                                    <b><FormattedMessage id="project.templates.addTemplateRow" /></b>
                                                </span>
                                            </Button>
                                        </td>
                                    )}
                                </tr>
                            </tbody>
                        </Table>
                    </div>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Col>
                <Col xs={12} sm={12} md={3} className='comment'>
                    <h3 className="title comments">
                        Comentarios
                    </h3>
                </Col>

            </Row>

        </div >

    );

}

export default TemplateView;
