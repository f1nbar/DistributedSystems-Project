import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import Form from 'react-bootstrap/Form'
import ListGroup from 'react-bootstrap/ListGroup'
import React from 'react';
import Axios from 'axios'
import DeliverySuccessModal from './DeliverySuccessModal'

class PaymentModal extends React.Component {
  constructor(){
    super();
    this.state = {
      showHide : false,
      quotations: [],
      chosenService: null,
      deliverySuccess: false,
      deliveryInfo: null
    }
    this.toggleShow = this.toggleShow.bind(this)
    this.getQuotations = this.getQuotations.bind(this)
    this.chooseQuotation = this.chooseQuotation.bind(this)
    this.closeDeliverySuccess = this.closeDeliverySuccess.bind(this)
  }

  chooseQuotation(q) {
    this.setState({ 
      chosenQuotation: q
    })
  }

  getQuotations() {
    let lat = parseInt(document.getElementById("lat").value)
    let long = parseInt(document.getElementById("long").value)
    
    let packageInfo = this.props.product.packageInfo

    Axios.post('http://localhost:8800/request', {
      "sourceLon": 48,
      "sourceLat": 2,
      "destinationLon": long,
      "destinationLat": lat,
      "parcels": [
        {
          "weightKg": packageInfo.weight,
          "lengthCm": packageInfo.lengthCm,
          "widthCm": packageInfo.width,
          "heightCm": packageInfo.height,
        }
      ]
    }).then(response => {
      let quotationsList = []
      response.data.forEach(quotation => {
        console.log(quotation)
        quotationsList.push({
          postalServiceID: quotation.serviceID,
          postalService: quotation.providerName,
          price: quotation.price,
          long: long,
          lat: lat
        })
      })
      this.setState({
        quotations: quotationsList
      })
    })
  }

  toggleShow() {
    this.setState({ 
      showHide: !this.state.showHide,
      quotations: [],
      chosenQuotation: null
    })
  }

  deliveryPrice() {
    if (this.state.chosenQuotation != null) {
      return(<p>Delivery: {this.formatMoney(this.state.chosenQuotation.price)} - {this.state.chosenQuotation.postalService} </p>)
    }
  }

  deliverButton() {
      if (this.state.chosenQuotation != null) {
        return(<Button className="qOptButton" variant="outline-dark" onClick={() => {this.deliver()}}>Deliver</Button>)
    }
  }

  deliver() {
    let lat = this.state.chosenQuotation.lat
    let long = this.state.chosenQuotation.long
    
    let packageInfo = this.props.product.packageInfo

    Axios.post('http://localhost:8800/request', {
      "serviceID": this.state.chosenQuotation.postalServiceID,
      "sourceLon": 48,
      "sourceLat": 2,
      "destinationLon": long,
      "destinationLat": lat,
      "parcels": [
        {
          "weightKg": packageInfo.weight,
          "lengthCm": packageInfo.lengthCm,
          "widthCm": packageInfo.width,
          "heightCm": packageInfo.height,
        }
      ]
    }).then(response => {
      this.setState({
        deliverySuccess: true,
        deliveryInfo: {
          trackingID: response.trackingID,
          dateDelivered: response.orderDate
        }
      })
      this.toggleShow()
    })
  }

  deliverySuccessModal() {
    if (this.state.deliverySuccess) {
      return(<DeliverySuccessModal deliveryInfo={this.state.deliveryInfo} close={this.closeDeliverySuccess} />)
    }
    
  }

  closeDeliverySuccess() {
    this.setState({
      deliverySuccess: false,
      deliveryInfo: null
    })
  }

  formatMoney(number) {
    var formatter = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'EUR',
    });
    return formatter.format(number)
  }
  
  render() {

    return (
      <>
        <Button variant="outline-dark" onClick={this.toggleShow}>
          Buy Now!
        </Button>
  
        <Modal
          show={this.state.showHide}
          onHide={this.toggleShow}
          backdrop="static"
          keyboard={false}
        >
          <Modal.Header closeButton>
            <Modal.Title>{this.props.product.name}</Modal.Title>
                <hr/>
                </Modal.Header>
                    <Modal.Body>
                        <p>Price: {this.formatMoney(this.props.product.price)}</p>
                        {this.deliveryPrice()}
                        <Form>
                            <p><u>Package Info:</u></p>
                            <p>Weight: {this.props.product.packageInfo.weight}kg</p>
                            <p>Volume: {this.props.product.packageInfo.lengthCm*this.props.product.packageInfo.width*this.props.product.packageInfo.height}cm<sup>3</sup></p>
                            <p><u>Please enter your delivery information</u></p>
                            <Form.Group>
                                <Form.Control id="lat" type="text" placeholder="Latitude" />
                            </Form.Group>
                            <Form.Group>
                                <Form.Control id="long" type="text" placeholder="Longitude" />
                            </Form.Group>
                            <Button variant="outline-dark" onClick={() => this.getQuotations()}>Get Delivery Quotes</Button>
                        </Form>
                        <ListGroup>
                          {this.state.quotations.map(quotation => {
                            console.log('quote')
                            return (
                              <ListGroup.Item >{quotation.postalService}: {this.formatMoney(quotation.price)} <Button className="qOptButton" variant="outline-dark" onClick={() => {this.chooseQuotation(quotation)}}>Choose Quotation</Button></ListGroup.Item>
                            )
                          })}
                        </ListGroup>
                    </Modal.Body>
                <Modal.Footer>
                  {this.deliverButton()}
                  <Button variant="secondary" onClick={this.toggleShow}>
                  Close
                  </Button>
                </Modal.Footer>
        </Modal>
        {this.deliverySuccessModal()}
      </>
    );
}
}

export default PaymentModal