// Code generated by github.com/99designs/gqlgen, DO NOT EDIT.

package resolvers

import (
	"fmt"
	"io"
	"strconv"

	"github.com/enmasseproject/enmasse/pkg/apis/enmasse/v1beta1"
	"github.com/enmasseproject/enmasse/pkg/consolegraphql"
	"k8s.io/apimachinery/pkg/apis/meta/v1"
)

type AddressQueryResultConsoleapiEnmasseIoV1beta1 struct {
	Total     int                                  `json:"Total"`
	Addresses []*AddressConsoleapiEnmasseIoV1beta1 `json:"Addresses"`
}

type AddressSpaceQueryResultConsoleapiEnmasseIoV1beta1 struct {
	Total         int                                       `json:"Total"`
	AddressSpaces []*AddressSpaceConsoleapiEnmasseIoV1beta1 `json:"AddressSpaces"`
}

type AddressSpaceConsoleapiEnmasseIoV1beta1 struct {
	ObjectMeta  *v1.ObjectMeta                                   `json:"ObjectMeta"`
	Spec        *v1beta1.AddressSpaceSpec                        `json:"Spec"`
	Status      *v1beta1.AddressSpaceStatus                      `json:"Status"`
	Connections *ConnectionQueryResultConsoleapiEnmasseIoV1beta1 `json:"Connections"`
	Addresses   *AddressQueryResultConsoleapiEnmasseIoV1beta1    `json:"Addresses"`
	Metrics     []*consolegraphql.Metric                         `json:"Metrics"`
}

type AddressConsoleapiEnmasseIoV1beta1 struct {
	ObjectMeta *v1.ObjectMeta                             `json:"ObjectMeta"`
	Spec       *v1beta1.AddressSpec                       `json:"Spec"`
	Status     *v1beta1.AddressStatus                     `json:"Status"`
	Links      *LinkQueryResultConsoleapiEnmasseIoV1beta1 `json:"Links"`
	Metrics    []*consolegraphql.Metric                   `json:"Metrics"`
}

type ConnectionQueryResultConsoleapiEnmasseIoV1beta1 struct {
	Total       int                                     `json:"Total"`
	Connections []*ConnectionConsoleapiEnmasseIoV1beta1 `json:"Connections"`
}

type ConnectionConsoleapiEnmasseIoV1beta1 struct {
	ObjectMeta *v1.ObjectMeta                             `json:"ObjectMeta"`
	Spec       *consolegraphql.ConnectionSpec             `json:"Spec"`
	Metrics    []*consolegraphql.Metric                   `json:"Metrics"`
	Links      *LinkQueryResultConsoleapiEnmasseIoV1beta1 `json:"Links"`
}

type KeyValue struct {
	Key   string `json:"Key"`
	Value string `json:"Value"`
}

type LinkQueryResultConsoleapiEnmasseIoV1beta1 struct {
	Total int                               `json:"Total"`
	Links []*LinkConsoleapiEnmasseIoV1beta1 `json:"Links"`
}

type LinkConsoleapiEnmasseIoV1beta1 struct {
	ObjectMeta *v1.ObjectMeta           `json:"ObjectMeta"`
	Spec       *consolegraphql.LinkSpec `json:"Spec"`
	Metrics    []*consolegraphql.Metric `json:"Metrics"`
}

type AddressSpaceType string

const (
	AddressSpaceTypeStandard AddressSpaceType = "standard"
	AddressSpaceTypeBrokered AddressSpaceType = "brokered"
)

var AllAddressSpaceType = []AddressSpaceType{
	AddressSpaceTypeStandard,
	AddressSpaceTypeBrokered,
}

func (e AddressSpaceType) IsValid() bool {
	switch e {
	case AddressSpaceTypeStandard, AddressSpaceTypeBrokered:
		return true
	}
	return false
}

func (e AddressSpaceType) String() string {
	return string(e)
}

func (e *AddressSpaceType) UnmarshalGQL(v interface{}) error {
	str, ok := v.(string)
	if !ok {
		return fmt.Errorf("enums must be strings")
	}

	*e = AddressSpaceType(str)
	if !e.IsValid() {
		return fmt.Errorf("%s is not a valid AddressSpaceType", str)
	}
	return nil
}

func (e AddressSpaceType) MarshalGQL(w io.Writer) {
	fmt.Fprint(w, strconv.Quote(e.String()))
}

type AddressType string

const (
	AddressTypeQueue        AddressType = "queue"
	AddressTypeTopic        AddressType = "topic"
	AddressTypeSubscription AddressType = "subscription"
	AddressTypeMulticast    AddressType = "multicast"
	AddressTypeAnycast      AddressType = "anycast"
)

var AllAddressType = []AddressType{
	AddressTypeQueue,
	AddressTypeTopic,
	AddressTypeSubscription,
	AddressTypeMulticast,
	AddressTypeAnycast,
}

func (e AddressType) IsValid() bool {
	switch e {
	case AddressTypeQueue, AddressTypeTopic, AddressTypeSubscription, AddressTypeMulticast, AddressTypeAnycast:
		return true
	}
	return false
}

func (e AddressType) String() string {
	return string(e)
}

func (e *AddressType) UnmarshalGQL(v interface{}) error {
	str, ok := v.(string)
	if !ok {
		return fmt.Errorf("enums must be strings")
	}

	*e = AddressType(str)
	if !e.IsValid() {
		return fmt.Errorf("%s is not a valid AddressType", str)
	}
	return nil
}

func (e AddressType) MarshalGQL(w io.Writer) {
	fmt.Fprint(w, strconv.Quote(e.String()))
}

type LinkRole string

const (
	LinkRoleSender   LinkRole = "sender"
	LinkRoleReceiver LinkRole = "receiver"
)

var AllLinkRole = []LinkRole{
	LinkRoleSender,
	LinkRoleReceiver,
}

func (e LinkRole) IsValid() bool {
	switch e {
	case LinkRoleSender, LinkRoleReceiver:
		return true
	}
	return false
}

func (e LinkRole) String() string {
	return string(e)
}

func (e *LinkRole) UnmarshalGQL(v interface{}) error {
	str, ok := v.(string)
	if !ok {
		return fmt.Errorf("enums must be strings")
	}

	*e = LinkRole(str)
	if !e.IsValid() {
		return fmt.Errorf("%s is not a valid LinkRole", str)
	}
	return nil
}

func (e LinkRole) MarshalGQL(w io.Writer) {
	fmt.Fprint(w, strconv.Quote(e.String()))
}

type MetricType string

const (
	MetricTypeGauge   MetricType = "gauge"
	MetricTypeCounter MetricType = "counter"
	MetricTypeRate    MetricType = "rate"
)

var AllMetricType = []MetricType{
	MetricTypeGauge,
	MetricTypeCounter,
	MetricTypeRate,
}

func (e MetricType) IsValid() bool {
	switch e {
	case MetricTypeGauge, MetricTypeCounter, MetricTypeRate:
		return true
	}
	return false
}

func (e MetricType) String() string {
	return string(e)
}

func (e *MetricType) UnmarshalGQL(v interface{}) error {
	str, ok := v.(string)
	if !ok {
		return fmt.Errorf("enums must be strings")
	}

	*e = MetricType(str)
	if !e.IsValid() {
		return fmt.Errorf("%s is not a valid MetricType", str)
	}
	return nil
}

func (e MetricType) MarshalGQL(w io.Writer) {
	fmt.Fprint(w, strconv.Quote(e.String()))
}

type Protocol string

const (
	ProtocolAmqp  Protocol = "amqp"
	ProtocolAmqps Protocol = "amqps"
)

var AllProtocol = []Protocol{
	ProtocolAmqp,
	ProtocolAmqps,
}

func (e Protocol) IsValid() bool {
	switch e {
	case ProtocolAmqp, ProtocolAmqps:
		return true
	}
	return false
}

func (e Protocol) String() string {
	return string(e)
}

func (e *Protocol) UnmarshalGQL(v interface{}) error {
	str, ok := v.(string)
	if !ok {
		return fmt.Errorf("enums must be strings")
	}

	*e = Protocol(str)
	if !e.IsValid() {
		return fmt.Errorf("%s is not a valid Protocol", str)
	}
	return nil
}

func (e Protocol) MarshalGQL(w io.Writer) {
	fmt.Fprint(w, strconv.Quote(e.String()))
}